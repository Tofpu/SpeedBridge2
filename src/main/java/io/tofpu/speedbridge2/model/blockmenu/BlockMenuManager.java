package io.tofpu.speedbridge2.model.blockmenu;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.tofpu.speedbridge2.model.blockmenu.holder.BlockMenuHolder;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class BlockMenuManager {
    private static final int BLOCK_CHANGE_COOLDOWN = 30;

    public static final BlockMenuManager INSTANCE = new BlockMenuManager();

    private final Set<Material> materialSet = new HashSet<>();
    private final Cache<UUID, Long> cooldownMap;

    private BlockMenuManager() {
        this.cooldownMap = Caffeine.newBuilder()
                .expireAfterWrite(BLOCK_CHANGE_COOLDOWN, TimeUnit.SECONDS)
                .build();
    }

    public void load() {
        reload();
    }

    public void reload() {
        materialSet.clear();
        materialSet.addAll(ConfigurationManager.INSTANCE.getBlockMenuCategory()
                .getMaterialBlocks());

        final Inventory inventory = BlockMenuHolder.INSTANCE.getInventory();
        inventory.clear();

        final AtomicInteger index = new AtomicInteger(10);
        materialSet.forEach(material -> {
            final ItemStack item = new ItemStack(material);
            final ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(BridgeUtil.translate("&6" + WordUtils.capitalizeFully(material.name().replace("_", " "))));
            itemMeta.setLore(Collections.singletonList(BridgeUtil.translate(
                    "&eClick " + "this " + "block to " + "select it" + "!")));
            item.setItemMeta(itemMeta);

            inventory.setItem(index.getAndIncrement(), item);
        });
    }

    public Inventory showInventory(final BridgePlayer bridgePlayer) {
        final Player player = bridgePlayer.getPlayer();
        if (player == null) {
            return null;
        }

        final Inventory inventoryClone = cloneInventory(BlockMenuHolder.INSTANCE.getInventory());

        final Material chosenMaterial = bridgePlayer.getChoseMaterial();
        for (final ItemStack itemStack : inventoryClone.getContents()) {
            final Material itemType =
                    itemStack == null ? Material.AIR : itemStack.getType();

            if (itemType == Material.AIR) {
                continue;
            }

            if (itemType == chosenMaterial) {
                modifyItem(ModifyItemType.SELECTED, itemStack);
                continue;
            }

            final boolean canSelectItem = canSelectItem(bridgePlayer, itemStack);

            // if the player cannot select the item
            if (!canSelectItem) {
                modifyItem(ModifyItemType.LACK_PERMISSION, itemStack);
            }
        }

        player.openInventory(inventoryClone);
        return inventoryClone;
    }

    public boolean canSelectItem(final BridgePlayer bridgePlayer, final ItemStack itemStack) {
        final Material type = itemStack == null ? Material.AIR : itemStack.getType();
        if (type == Material.AIR) {
            return false;
        }
        return ConfigurationManager.INSTANCE.getBlockMenuCategory()
                       .getDefaultBlock() == type || bridgePlayer.getPlayer()
                       .hasPermission("speedbridge.block." + type.name());
    }

    private void modifyItem(final ModifyItemType modifyItemType, final ItemStack itemStack) {
        switch (modifyItemType) {
            case SELECTED:
                chosenItem(itemStack);
                break;
            case LACK_PERMISSION:
                lackPermissionItem(itemStack);
                break;
        }
    }

    private Inventory cloneInventory(final @NotNull Inventory inventory) {
        final Inventory inventoryClone =
                Bukkit.createInventory(BlockMenuHolder.INSTANCE, inventory.getSize(),
                        BlockMenuHolder.INVENTORY_TITLE);
        inventoryClone.setContents(inventory.getContents());

        return inventoryClone;
    }

    private void chosenItem(final @NotNull ItemStack itemStack) {
        final ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.setDisplayName(BridgeUtil.translate(
                "&e&lSELECTED | " + meta.getDisplayName()));
        meta.setLore(Collections.singletonList(BridgeUtil.translate(
                "&eYou have " + "selected this " + "block!")));

        itemStack.setItemMeta(meta);
    }

    private void lackPermissionItem(final ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        // set the itemstack lore to the appropriate lore
        itemMeta.setLore(Collections.singletonList(BridgeUtil.translate("&cYou do not have the permission to use this!")));

        itemStack.setItemMeta(itemMeta);
    }

    public boolean isInCooldown(final UUID uniqueId, final Consumer<Long> inCooldownConsumer) {
        final Long cooldown = cooldownMap.getIfPresent(uniqueId);
        if (cooldown == null) {
            return false;
        }

        final long now = System.currentTimeMillis();
        final long remainder = (now - cooldown) / 1000;

        inCooldownConsumer.accept(remainder);
        return true;
    }

    public void putInCooldown(final UUID uniqueId) {
        cooldownMap.put(uniqueId, System.currentTimeMillis());
    }

    enum ModifyItemType {
        SELECTED, LACK_PERMISSION;
    }
}
