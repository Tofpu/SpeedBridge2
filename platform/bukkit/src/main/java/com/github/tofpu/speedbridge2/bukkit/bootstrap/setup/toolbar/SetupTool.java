package com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar;

import io.tofpu.toolbar.toolbar.tool.Tool;
import io.tofpu.toolbar.toolbar.tool.action.ToolActionTypes;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SetupTool extends Tool {
    public SetupTool(String id, ItemStack item, Consumer<PlayerInteractEvent> action) {
        super(id, item, (ToolActionTypes.PlayerInteract) (toolbar, playerInteractEvent) -> action.accept(playerInteractEvent));
    }
}
