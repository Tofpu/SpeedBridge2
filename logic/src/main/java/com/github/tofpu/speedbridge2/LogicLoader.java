package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.game.island.IslandGameHandler;
import com.github.tofpu.speedbridge2.game.island.arena.IslandArenaManager;
import com.github.tofpu.speedbridge2.lobby.LobbyService;

public class LogicLoader {

    private final SpeedBridge speedBridge;

    public LogicLoader(SpeedBridge speedBridge) {
        this.speedBridge = speedBridge;
    }

    public static LogicLoader load(SpeedBridge speedBridge) {
        LogicLoader logicLoader = new LogicLoader(speedBridge);
        logicLoader.load();
        return logicLoader;
    }

    public void load() {
        speedBridge.serviceManager().register(new LobbyService(speedBridge.serviceManager()));
    }

    public void enable(LogicBootStrap bootStrap) {
        initGame(bootStrap);
    }

    private void initGame(LogicBootStrap bootStrap) {
        ArenaAdapter arenaAdapter = bootStrap.arenaAdapter();
        new IslandGameHandler(speedBridge.serviceManager().get(LobbyService.class), new IslandArenaManager(arenaAdapter.gameWorld(), arenaAdapter.clipboardPaster()));
    }

    public void disable() {
    }
}