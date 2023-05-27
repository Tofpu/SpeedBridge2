package com.github.tofpu.speedbridge2;

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

    public void enable() {

    }

    public void disable() {
    }
}