package com.github.tofpu.speedbridge2.common;

import com.github.tofpu.speedbridge2.CoreApplication;
import com.github.tofpu.speedbridge2.common.bridge.game.BridgeGameHandlerBuilder;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.common.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.bridge.setup.BridgeSetupHandler;
import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

public class CommonApplication {

    private final CoreApplication speedBridge;
    private IslandGameHandler gameHandler;
    private BridgeSetupHandler islandSetupHandler;
    private IslandSetupController setupController;
    private SchematicHandler schematicHandler;

    public CommonApplication(CoreApplication speedBridge) {
        this.speedBridge = speedBridge;
    }

    public void load() {
        speedBridge.serviceManager().register(LobbyService.class, LobbyService::new);
        speedBridge.serviceManager().register(IslandService.class, serviceManager -> new IslandService(serviceManager.get(DatabaseService.class)));
        speedBridge.serviceManager().register(BridgeScoreService.class, BridgeScoreService::new);
    }

    public void enable(CommonBootstrap bootStrap) {
        PlatformArenaAdapter arenaAdapter = bootStrap.arenaAdapter();
        schematicHandler = SchematicHandler.load(bootStrap.schematicFolder(), arenaAdapter.schematicResolver(), arenaAdapter.schematicPredicate());

        initGame(bootStrap.gameAdapter(), arenaAdapter, speedBridge.serviceManager());
        initSetupGame(arenaAdapter, speedBridge.serviceManager());
    }

    private void initSetupGame(PlatformArenaAdapter arenaAdapter, ServiceManager serviceManager) {
        BridgeSetupHandler setupHandler = new BridgeSetupHandler(serviceManager.get(IslandService.class), serviceManager.get(LobbyService.class), arenaAdapter, schematicHandler);
        setupController = new IslandSetupController(setupHandler);
    }

    private void initGame(PlatformGameAdapter gameAdapter, PlatformArenaAdapter arenaAdapter, ServiceManager serviceManager) {
        arenaAdapter.resetAndLoadGameWorld();
        gameHandler = BridgeGameHandlerBuilder.newBuilder(arenaAdapter)
                .coreStateProvider(gameAdapter, serviceManager.get(LobbyService.class))
                .gameStateProvider(gameAdapter, serviceManager.get(EventDispatcherService.class), serviceManager.get(BridgeScoreService.class))
                .build(arenaAdapter, schematicHandler);
    }

    public void disable() {
    }

    public IslandGameHandler gameHandler() {
        return gameHandler;
    }

    public IslandSetupController setupController() {
        return setupController;
    }

    public SchematicHandler schematicHandler() {
        return schematicHandler;
    }
}