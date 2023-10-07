package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.BridgeGameHandlerBuilder;
import com.github.tofpu.speedbridge2.bridge.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.bridge.setup.BridgeSetupHandler;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.bridge.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

public class LogicLoader {

    private final SpeedBridge speedBridge;
    private IslandGameHandler gameHandler;
    private BridgeSetupHandler islandSetupHandler;
    private IslandSetupController setupController;
    private SchematicHandler schematicHandler;

    public LogicLoader(SpeedBridge speedBridge) {
        this.speedBridge = speedBridge;
    }

    public static LogicLoader load(SpeedBridge speedBridge) {
        LogicLoader logicLoader = new LogicLoader(speedBridge);
        logicLoader.load();
        return logicLoader;
    }

    public void load() {
        speedBridge.serviceManager().register(LobbyService.class, LobbyService::new);
        speedBridge.serviceManager().register(IslandService.class, serviceManager -> new IslandService(serviceManager.get(DatabaseService.class)));
        speedBridge.serviceManager().register(BridgeScoreService.class, BridgeScoreService::new);
    }

    public void enable(LogicBootStrap bootStrap) {
        ArenaAdapter arenaAdapter = bootStrap.arenaAdapter();
        schematicHandler = SchematicHandler.load(bootStrap.schematicFolder(), arenaAdapter.schematicResolver(), arenaAdapter.schematicPredicate());

        initGame(bootStrap.gameAdapter(), arenaAdapter, speedBridge.serviceManager());
        initSetupGame(arenaAdapter, speedBridge.serviceManager());
    }

    private void initSetupGame(ArenaAdapter arenaAdapter, ServiceManager serviceManager) {
        BridgeSetupHandler setupHandler = new BridgeSetupHandler(serviceManager.get(IslandService.class), serviceManager.get(LobbyService.class), arenaAdapter, schematicHandler);
        setupController = new IslandSetupController(setupHandler);
    }

    private void initGame(GameAdapter gameAdapter, ArenaAdapter arenaAdapter, ServiceManager serviceManager) {
        arenaAdapter.resetAndLoadGameWorld();
        gameHandler = BridgeGameHandlerBuilder.newBuilder()
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