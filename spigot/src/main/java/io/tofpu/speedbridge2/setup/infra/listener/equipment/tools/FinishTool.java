package io.tofpu.speedbridge2.setup.infra.listener.equipment.tools;

import io.tofpu.speedbridge2.setup.service.SetupService;
import io.tofpu.toolbar.toolbar.tool.action.ToolAction;
import io.tofpu.toolbar.toolbar.tool.action.ToolActionUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FinishTool extends SetupTool {
    public FinishTool(SetupService setupService, String id, ItemStack item) {
        super(id, item, ToolActionUtil.listenFor(PlayerInteractEvent.class, eventHandler(setupService)));
    }

    private static @NotNull ToolAction<PlayerInteractEvent> eventHandler(SetupService setupService) {
        return (genericToolbar, event) -> {
            setupService.finishSetup(event.getPlayer());
        };
    }
}
