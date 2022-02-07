package io.tofpu.speedbridge2.domain.blockmenu;

import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        materialSet.addAll(ConfigurationManager.INSTANCE.getBlockMenuCategory().getMaterialBlocks());

        final Inventory inventory = BlockMenuHolder.INSTANCE.getInventory();
        inventory.clear();

        final AtomicInteger index = new AtomicInteger(10);
        materialSet.forEach(material -> inventory.setItem(index.getAndIncrement(), new ItemStack(material)));
    }

    public Inventory showInventory(final BridgePlayer bridgePlayer) {
        final Player player = bridgePlayer.getPlayer();
        if (player == null) {
            return null;
        }

        final Inventory inventory = BlockMenuHolder.INSTANCE.getInventory();
        player.openInventory(inventory);
        return inventory;
    }

    public Set<Material> getMaterialSet() {
        return Collections.unmodifiableSet(materialSet);
    }
}
