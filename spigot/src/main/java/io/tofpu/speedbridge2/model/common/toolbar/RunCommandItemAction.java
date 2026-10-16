package io.tofpu.speedbridge2.model.common.toolbar;

import io.tofpu.speedbridge2.model.island.object.GameIsland;
import com.tofpu.toolbar.api.toolbar.Toolbar;
import com.tofpu.toolbar.api.toolbar.tool.action.ToolAction;
import org.bukkit.event.player.PlayerInteractEvent;

public class RunCommandItemAction implements ToolAction<PlayerInteractEvent> {
    private final String command;
    private final GameIsland gameIsland;

    public RunCommandItemAction(final String command, final GameIsland gameIsland) {
        this.command = command;
        this.gameIsland = gameIsland;
    }

    @Override
    public void handle(Toolbar toolbar, PlayerInteractEvent event) {
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
