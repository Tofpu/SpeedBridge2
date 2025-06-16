package io.tofpu.speedbridge2.lobby.system;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.lobby.LobbyService;
import io.tofpu.speedbridge2.lobby.command.LobbyCommand;

public class LobbySystem {
    private final LobbyService lobbyService;

    public LobbySystem() {
        this.lobbyService = new LobbyService();
    }

    public void registerCommands(CommandHandler handler) {
        handler.addChildCommand(new LobbyCommand(lobbyService));
    }

    public LobbyService lobbyService() {
        return lobbyService;
    }
}
