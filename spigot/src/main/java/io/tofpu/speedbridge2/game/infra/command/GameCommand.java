package io.tofpu.speedbridge2.game.infra.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
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
            actor.reply(String.format("You joined island %d", island.slot()));
        } else {
            actor.reply("&cYou're already in a game!");
        }
    }

    @Subcommand("leave")
    public void leaveGame(BukkitCommandActor actor) {
        if (gameService.stopGame(actor.requirePlayer())) {
            actor.reply("&eYou left the game");
        } else {
            actor.reply("&cYou're not in a game!");
        }
    }
}
