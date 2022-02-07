package io.tofpu.speedbridge2.domain.blockmenu;

import io.tofpu.speedbridge2.domain.blockmenu.holder.BlockMenuHolder;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
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
import java.util.concurrent.atomic.AtomicInteger;

public final class BlockMenuManager {
    public static final BlockMenuManager INSTANCE = new BlockMenuManager();

    private final Set<Material> materialSet = new HashSet<>();

    private BlockMenuManager() {}

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
            itemMeta.setDisplayName(BridgeUtil.translate("&6" + material.name()));
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
            if (itemStack == null || itemStack.getType() != chosenMaterial) {
                continue;
            }

            chosenItem(itemStack);
            break;
        }

        player.openInventory(inventoryClone);
        return inventoryClone;
    }

    private Inventory cloneInventory(final @NotNull Inventory inventory) {
        final Inventory inventoryClone = Bukkit.createInventory(BlockMenuHolder.INSTANCE, inventory.getSize(), inventory.getTitle());
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

    public Set<Material> getMaterialSet() {
        return Collections.unmodifiableSet(materialSet);
    }
}
