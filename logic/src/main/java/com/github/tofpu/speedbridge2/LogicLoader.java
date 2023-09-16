package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.game.island.IslandGameHandler;
import com.github.tofpu.speedbridge2.game.island.arena.IslandArenaManager;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.island.setup.IslandSetupHandler;
import com.github.tofpu.speedbridge2.island.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

public class LogicLoader {

    private final SpeedBridge speedBridge;
    private IslandGameHandler gameHandler;
    private IslandSetupHandler islandSetupHandler;
    private IslandSetupController setupController;

    public LogicLoader(SpeedBridge speedBridge) {
        this.speedBridge = speedBridge;
    }

    public static LogicLoader load(SpeedBridge speedBridge) {
        LogicLoader logicLoader = new LogicLoader(speedBridge);
        logicLoader.load();
        return logicLoader;
    }

    public void load() {
        LobbyService lobbyService = new LobbyService(speedBridge.serviceManager());
        speedBridge.serviceManager().register(lobbyService);

        IslandService islandService = new IslandService(speedBridge.serviceManager().get(DatabaseService.class));
        speedBridge.serviceManager().register(islandService);
    }

    public void enable(LogicBootStrap bootStrap) {
        initGame(bootStrap);
    }

    private void initGame(LogicBootStrap bootStrap) {
        ArenaAdapter arenaAdapter = bootStrap.arenaAdapter();

        ServiceManager serviceManager = speedBridge.serviceManager();
        gameHandler = new IslandGameHandler(serviceManager.get(LobbyService.class), new IslandArenaManager(arenaAdapter.gameWorld(), arenaAdapter.clipboardPaster()));
        IslandSetupHandler setupHandler = new IslandSetupHandler(serviceManager.get(IslandService.class), serviceManager.get(LobbyService.class), new IslandArenaManager(arenaAdapter.gameWorld(), arenaAdapter.clipboardPaster()), arenaAdapter.gameWorld());
        setupController = new IslandSetupController(setupHandler);
    }

    public void disable() {
    }

    public IslandGameHandler gameHandler() {
        return gameHandler;
    }

    public IslandSetupController setupController() {
        return setupController;
    }
}