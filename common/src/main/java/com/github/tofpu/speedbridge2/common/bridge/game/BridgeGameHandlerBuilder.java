package com.github.tofpu.speedbridge2.common.bridge.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.land.LandController;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;

public class BridgeGameHandlerBuilder {

    private final LandController landController;

    public static BridgeGameHandlerBuilder newBuilder(PlatformArenaAdapter arenaAdapter) {
        return new BridgeGameHandlerBuilder(new LandController(new IslandArenaManager(arenaAdapter)));
    }

    private BridgeGameHandlerBuilder(LandController landController) {
        // prevents direct initialization
        this.landController = landController;
    }

    public IslandGameHandler build(PlatformArenaAdapter arenaAdapter, SchematicHandler schematicHandler) {
        return new IslandGameHandler(schematicHandler, landController, arenaAdapter);
    }
}
