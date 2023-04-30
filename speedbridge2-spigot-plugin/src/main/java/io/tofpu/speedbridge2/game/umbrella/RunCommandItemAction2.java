package io.tofpu.speedbridge2.game.umbrella;

import io.tofpu.speedbridge2.game.GameSession;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.umbrella.domain.Umbrella;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.event.player.PlayerInteractEvent;

public class RunCommandItemAction2 extends AbstractItemAction {
    private final String command;
    private final GameSession gameIsland;

    public RunCommandItemAction2(final String command, final GameSession gameSession) {
        this.command = command;
        this.gameIsland = gameSession;
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
