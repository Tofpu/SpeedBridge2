package io.tofpu.speedbridge2.game.system;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.game.infra.command.GameCommandHandler;
import io.tofpu.speedbridge2.game.infra.config.GameConfigManager;
import io.tofpu.speedbridge2.game.infra.listener.GameListener;
import io.tofpu.speedbridge2.game.infra.listener.GameStateListener;
import io.tofpu.speedbridge2.game.infra.listener.blocktracker.BlockPlacementTrackerListener;
import io.tofpu.speedbridge2.game.infra.listener.equipment.GameEquipmentLifecycle;
import io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.GameEquipmentHandler;
import io.tofpu.speedbridge2.game.service.GameService;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import io.tofpu.speedbridge2.util.listener.ListenerRegistration;
import io.tofpu.toolbar.ToolbarAPI;
import org.bukkit.World;

import java.io.File;

public class GameSystem {
    private final EventBus eventBus;
    private final GameService gameService;
    private final GameConfigManager gameConfigManager;

    public GameSystem(EventBus eventBus, File dataDirectory, LobbyTeleporter lobbyTeleporter, World arenaWorld) {
        this.eventBus = eventBus;
        this.gameConfigManager = new GameConfigManager(dataDirectory);
        this.gameService = new GameService(
                eventBus,
                arenaWorld,
                gameConfigManager,
                lobbyTeleporter
        );
    }

    public void registerListeners(ListenerRegistration listenerRegistration, ToolbarAPI toolbarAPI) {
        listenerRegistration.register(new GameListener(gameService));
        listenerRegistration.register(new GameStateListener(gameService));

        GameEquipmentHandler equipmentHandler = new GameEquipmentHandler(gameConfigManager, toolbarAPI);
        equipmentHandler.register(gameService);

        new BlockPlacementTrackerListener(gameService).register(listenerRegistration, eventBus);
        new GameEquipmentLifecycle(equipmentHandler).register(eventBus);
    }

    public void enable() {
        gameConfigManager.load();
    }

    public void registerCommand(CommandHandler handler) {
        GameCommandHandler.init(handler, gameService);
    }

    public GameService getGameService() {
        return gameService;
    }
}
