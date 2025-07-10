package io.tofpu.speedbridge2.game.infra.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.game.infra.message.GameMessage;
import io.tofpu.speedbridge2.game.service.GameService;
import io.tofpu.speedbridge2.island.domain.Island;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

@Subcommand("game")
public class GameCommand extends ChildrenCommand {
    private final GameService gameService;

    public GameCommand(GameService gameService) {
        this.gameService = gameService;
    }

    @Subcommand("join")
    public void joinGame(BukkitCommandActor actor, Island island) {
        if (gameService.startGame(actor.requirePlayer(), island)) {
            GameMessage.GAME_JOINED.of(island.slot())
                    .send(actor.sender());
        } else {
            GameMessage.ALREADY_IN_GAME.of(island.slot())
                    .send(actor.sender());
        }
    }

    @Subcommand("leave")
    public void leaveGame(BukkitCommandActor actor) {
        if (gameService.stopGame(actor.requirePlayer())) {
            GameMessage.GAME_LEFT.of()
                    .send(actor.sender());
        } else {
            GameMessage.NOT_IN_GAME.of()
                    .send(actor.sender());
        }
    }
}
