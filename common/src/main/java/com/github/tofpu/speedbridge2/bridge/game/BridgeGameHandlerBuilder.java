package com.github.tofpu.speedbridge2.bridge.game;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.game.land.LandController;
import com.github.tofpu.speedbridge2.bridge.game.state.GameStateHandler;
import com.github.tofpu.speedbridge2.bridge.game.state.basic.BridgeBasicStateProvider;
import com.github.tofpu.speedbridge2.bridge.game.state.game.BridgeGameStateProvider;
import com.github.tofpu.speedbridge2.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;

import java.util.Objects;

public class BridgeGameHandlerBuilder {

    private final LandController landController;
    private BridgeBasicStateProvider coreStateProvider;
    private BridgeGameStateProvider.Builder gameStateProviderBuilder;

    public static BridgeGameHandlerBuilder newBuilder(ArenaAdapter arenaAdapter) {
        return new BridgeGameHandlerBuilder(new LandController(new IslandArenaManager(arenaAdapter)));
    }

    private BridgeGameHandlerBuilder(LandController landController) {
        // prevents direct initialization
        this.landController = landController;
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

    public IslandGameHandler build(ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        Objects.requireNonNull(coreStateProvider, "BridgeCoreStateProvider must not be null.");
        Objects.requireNonNull(gameStateProviderBuilder, "BridgeGameStateProvider must not be null.");
        GameStateHandler gameStateHandler = new GameStateHandler(coreStateProvider, gameStateProviderBuilder);
        return IslandGameHandler.create(coreStateProvider, gameStateHandler, arenaAdapter, schematicHandler);
    }
}
