package com.github.tofpu.speedbridge2.lobby;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.listener.Listener;
import com.github.tofpu.speedbridge2.listener.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.listener.event.PlayerJoinEvent;
import com.github.tofpu.speedbridge2.message.EnumMessage;
import com.github.tofpu.speedbridge2.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

public class LobbyListener implements Listener {
    private final LobbyService lobbyService;

    public LobbyListener(ServiceManager serviceManager) {
        this(serviceManager.get(LobbyService.class));
    }

    public LobbyListener(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @EventListener
    void on(final PlayerJoinEvent event) {
        OnlinePlayer player = event.getPlayer();
        boolean lobbyAvailable = lobbyService.isLobbyAvailable();

        if (!lobbyAvailable && player.isOperator()) {
            player.sendMessage(EnumMessage.MISSING_LOBBY);
        } else {
            player.teleport(lobbyService.lobby().getPosition());
        }
    }
}
