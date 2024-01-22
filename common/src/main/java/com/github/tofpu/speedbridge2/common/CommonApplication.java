package com.github.tofpu.speedbridge2.common;

import com.github.tofpu.speedbridge2.CoreApplication;
import com.github.tofpu.speedbridge2.common.bridge.BridgeSystem;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.common.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.bridge.setup.BridgeSetupHandler;
import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

public class CommonApplication {

    private final CoreApplication speedBridge;
    private IslandSetupController setupController;
    private SchematicHandler schematicHandler;
    private BridgeSystem bridgeSystem;

    public CommonApplication(CoreApplication speedBridge) {
        this.speedBridge = speedBridge;
    }

    public void load() {
        speedBridge.serviceManager().register(LobbyService.class, LobbyService::new);
        speedBridge.serviceManager().register(IslandService.class, IslandService::new);
        speedBridge.serviceManager().register(BridgeScoreService.class, BridgeScoreService::new);
    }

    public void enable(CommonBootstrap bootStrap) {
        PlatformArenaAdapter arenaAdapter = bootStrap.arenaAdapter();
        schematicHandler = SchematicHandler.load(bootStrap.schematicFolder(), arenaAdapter.schematicResolver(), arenaAdapter.schematicPredicate());

        initGame(bootStrap.gameAdapter(), arenaAdapter, speedBridge.serviceManager());
        initSetupGame(arenaAdapter, speedBridge.serviceManager());
    }

    private void initSetupGame(PlatformArenaAdapter arenaAdapter, ServiceManager serviceManager) {
        BridgeSetupHandler setupHandler = new BridgeSetupHandler(serviceManager, arenaAdapter, schematicHandler);
        setupController = new IslandSetupController(setupHandler);
    }

    private void initGame(PlatformGameAdapter gameAdapter, PlatformArenaAdapter arenaAdapter, ServiceManager serviceManager) {
        arenaAdapter.resetAndLoadGameWorld();
        bridgeSystem = new BridgeSystem(serviceManager.get(EventDispatcherService.class), schematicHandler, arenaAdapter);
        bridgeSystem.registerListener(gameAdapter, serviceManager);
    }

    public void disable() {
    }

    public BridgeSystem bridgeSystem() {
        return bridgeSystem;
    }

    public IslandSetupController setupController() {
        return setupController;
    }

    public SchematicHandler schematicHandler() {
        return schematicHandler;
    }
}