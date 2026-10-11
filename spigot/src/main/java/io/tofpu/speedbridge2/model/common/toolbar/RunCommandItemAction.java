package io.tofpu.speedbridge2.model.common.toolbar;

import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.toolbar.toolbar.GenericToolbar;
import io.tofpu.toolbar.toolbar.tool.action.ToolAction;
import org.bukkit.event.player.PlayerInteractEvent;

public class RunCommandItemAction implements ToolAction<PlayerInteractEvent> {
    private final String command;
    private final GameIsland gameIsland;

    public RunCommandItemAction(final String command, final GameIsland gameIsland) {
        this.command = command;
        this.gameIsland = gameIsland;
    }

    @Override
    public void handle(GenericToolbar<?> genericToolbar, PlayerInteractEvent event) {
        if (!command.equalsIgnoreCase("RESET_ISLAND")) {
            event.getPlayer().performCommand(command);
            return;
        }

        if (gameIsland == null) {
            return;
        }
        gameIsland.resetGame();
    }

    public String getCommand() {
        return command;
    }

    @Override
    public Class<PlayerInteractEvent> type() {
        return PlayerInteractEvent.class;
    }
}
