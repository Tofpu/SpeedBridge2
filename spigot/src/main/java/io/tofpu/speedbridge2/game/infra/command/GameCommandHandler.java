package io.tofpu.speedbridge2.game.infra.command;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.game.service.GameService;

public class GameCommandHandler {
    private final CommandHandler commandHandler;
    private final GameService gameService;

    public static void init(CommandHandler commandHandler, GameService gameService) {
        new GameCommandHandler(commandHandler, gameService).register();
    }

    private GameCommandHandler(CommandHandler commandHandler, GameService gameService) {
        this.commandHandler = commandHandler;
        this.gameService = gameService;
    }

    public void register() {
        commandHandler.addChildCommand(new GameCommand(gameService));
    }
}
