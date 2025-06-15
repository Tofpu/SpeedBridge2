package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.environment.EnvironmentService;
import io.tofpu.speedbridge2.game.command.GameCommand;
import io.tofpu.speedbridge2.game.config.GameConfigManager;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import io.tofpu.speedbridge2.util.listener.ListenerRegistration;
import io.tofpu.toolbar.ToolbarAPI;

import java.io.File;

public class GameSystem {
    private final GameService gameService;
    private final GameConfigManager gameConfigManager;

    public GameSystem(EnvironmentService environmentService, File dataDirectory, ListenerRegistration listenerRegistration, LobbyTeleporter lobbyTeleporter, ToolbarAPI toolbarAPI) {
        gameConfigManager = new GameConfigManager(dataDirectory);
        this.gameService = new GameService(
                environmentService.getWorld(),
                gameConfigManager,
                listenerRegistration,
                lobbyTeleporter,
                toolbarAPI
        );
    }

    public void enable() {
        gameConfigManager.load();
        gameService.enable();
    }

    public void registerCommand(CommandHandler handler) {
        handler.addChildCommand(new GameCommand(gameService));
    }

    public GameService getGameService() {
        return gameService;
    }
}
