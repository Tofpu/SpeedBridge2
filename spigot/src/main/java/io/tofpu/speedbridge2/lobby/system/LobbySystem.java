package io.tofpu.speedbridge2.lobby.system;

import io.tofpu.speedbridge2.lobby.LobbyService;

public class LobbySystem {
    private final LobbyService lobbyService;

    public LobbySystem() {
        this.lobbyService = new LobbyService();
    }

    public LobbyService lobbyService() {
        return lobbyService;
    }
}
