package io.tofpu.speedbridge2.domain.blockmenu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class BlockMenuHolder implements InventoryHolder {
    public static final BlockMenuHolder INSTANCE = new BlockMenuHolder();

    private final Inventory inventory = Bukkit.createInventory(this, InventoryType.CHEST, "Block Menu");

    private BlockMenuHolder() {}

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
