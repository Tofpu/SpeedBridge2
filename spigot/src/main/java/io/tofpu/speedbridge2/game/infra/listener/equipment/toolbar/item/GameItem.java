package io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.item;

import io.tofpu.toolbar.toolbar.tool.Tool;
import io.tofpu.toolbar.toolbar.tool.action.ToolAction;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class GameItem extends Tool {
    public GameItem(String id, ItemStack item, ToolAction<? extends Event> action) {
        super(id, item, action);
    }

    public GameItem(String id, ItemStack item) {
        super(id, item);
    }
}
