package com.github.tofpu.speedbridge2.common.bridge.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.bridge.game.state.GameStateHandler;
import com.github.tofpu.speedbridge2.common.bridge.game.state.game.BridgeGameStateProvider;
import com.github.tofpu.speedbridge2.common.game.land.LandController;
import com.github.tofpu.speedbridge2.common.bridge.game.state.basic.BridgeBasicStateProvider;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

import java.util.Objects;

public class BridgeGameHandlerBuilder {

    private final LandController landController;
    private BridgeBasicStateProvider coreStateProvider;
    private BridgeGameStateProvider.Builder gameStateProviderBuilder;

    public static BridgeGameHandlerBuilder newBuilder(PlatformArenaAdapter arenaAdapter) {
        return new BridgeGameHandlerBuilder(new LandController(new IslandArenaManager(arenaAdapter)));
    }

    private BridgeGameHandlerBuilder(LandController landController) {
        // prevents direct initialization
        this.landController = landController;
    }

    public BridgeGameHandlerBuilder coreStateProvider(PlatformGameAdapter gameAdapter, ServiceManager serviceManager) {
        return coreStateProvider(gameAdapter, serviceManager.get(LobbyService.class));
    }

    public BridgeGameHandlerBuilder gameStateProvider(PlatformGameAdapter gameAdapter, ServiceManager serviceManager) {
        return gameStateProvider(gameAdapter, serviceManager.get(EventDispatcherService.class), serviceManager.get(BridgeScoreService.class));
    }

    public BridgeGameHandlerBuilder coreStateProvider(PlatformGameAdapter gameAdapter, LobbyService lobbyService) {
        coreStateProvider = new BridgeBasicStateProvider(gameAdapter, lobbyService, landController);
        return this;
    }

    public BridgeGameHandlerBuilder gameStateProvider(PlatformGameAdapter gameAdapter, EventDispatcherService eventDispatcherService, BridgeScoreService scoreService) {
        gameStateProviderBuilder = BridgeGameStateProvider.newBuilder()
                .setGameAdapter(gameAdapter)
                .setEventDispatcherService(eventDispatcherService)
                .setScoreService(scoreService);
        return this;
    }

    public IslandGameHandler build(PlatformArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        Objects.requireNonNull(coreStateProvider, "BridgeCoreStateProvider must not be null.");
        Objects.requireNonNull(gameStateProviderBuilder, "BridgeGameStateProvider must not be null.");
        GameStateHandler gameStateHandler = new GameStateHandler(coreStateProvider, gameStateProviderBuilder);
        return IslandGameHandler.create(coreStateProvider, gameStateHandler, arenaAdapter, landController, schematicHandler);
    }
}
