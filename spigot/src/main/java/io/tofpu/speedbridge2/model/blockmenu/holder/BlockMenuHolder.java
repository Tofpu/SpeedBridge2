package io.tofpu.speedbridge2.model.blockmenu.holder;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class BlockMenuHolder implements InventoryHolder {
    public static final String INVENTORY_TITLE = "Blocks Menu";
    public static final BlockMenuHolder INSTANCE = new BlockMenuHolder();

    private final Inventory inventory = Bukkit.createInventory(this,
            InventoryType.CHEST, INVENTORY_TITLE);

    private BlockMenuHolder() {
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
