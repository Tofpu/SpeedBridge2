package com.github.tofpu.speedbridge2.common.lobby;

import com.github.tofpu.speedbridge2.common.message.CommonMessages;
import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.event.PlayerJoinEvent;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
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
            player.sendMessage(CommonMessages.MISSING_LOBBY);
        } else {
            player.teleport(lobbyService.position());
        }
    }
}
