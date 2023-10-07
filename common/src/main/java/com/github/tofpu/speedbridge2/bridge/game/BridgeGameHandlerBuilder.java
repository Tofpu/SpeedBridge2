package com.github.tofpu.speedbridge2.bridge.game;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.game.state.core.BridgeCoreStateProvider;
import com.github.tofpu.speedbridge2.bridge.game.state.custom.BridgeGameStateProvider;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;

import java.util.Objects;

public class BridgeGameHandlerBuilder {

    private BridgeCoreStateProvider coreStateProvider;
    private BridgeGameStateProvider gameStateProvider;

    public static BridgeGameHandlerBuilder newBuilder() {
        return new BridgeGameHandlerBuilder();
    }

    private BridgeGameHandlerBuilder() {
        // prevents direct initialization
    }

    public BridgeGameHandlerBuilder coreStateProvider(GameAdapter gameAdapter, LobbyService lobbyService) {
        coreStateProvider = new BridgeCoreStateProvider(gameAdapter, lobbyService);
        return this;
    }

    public BridgeGameHandlerBuilder gameStateProvider(GameAdapter gameAdapter, EventDispatcherService eventDispatcherService) {
        gameStateProvider = new BridgeGameStateProvider(gameAdapter, eventDispatcherService);
        return this;
    }

    public IslandGameHandler build(ArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        Objects.requireNonNull(coreStateProvider, "BridgeCoreStateProvider must not be null.");
        Objects.requireNonNull(gameStateProvider, "BridgeGameStateProvider must not be null.");
        return IslandGameHandler.create(coreStateProvider, gameStateProvider, arenaAdapter, schematicHandler);
    }
}
