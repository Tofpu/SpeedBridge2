package io.tofpu.speedbridge2.setup.infra.listener.equipment.tools;

import io.tofpu.toolbar.toolbar.tool.Tool;
import io.tofpu.toolbar.toolbar.tool.action.ToolAction;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class SetupTool extends Tool {
    public SetupTool(String id, ItemStack item, ToolAction<? extends Event> action) {
        super(id, item, action);
    }

    public SetupTool(String id, ItemStack item) {
        super(id, item);
    }
}
