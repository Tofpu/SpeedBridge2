package io.tofpu.speedbridge2.model.common.umbrella;

import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;
import io.tofpu.umbrella.domain.Umbrella;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.event.player.PlayerInteractEvent;

public class RunCommandItemAction extends AbstractItemAction {
    private final String command;
    private final GameIsland gameIsland;

    public RunCommandItemAction(final String command, final GameIsland gameIsland) {
        this.command = command;
        this.gameIsland = gameIsland;
    }

    @Override
    public void trigger(final Umbrella umbrella, final PlayerInteractEvent event) {
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
}
