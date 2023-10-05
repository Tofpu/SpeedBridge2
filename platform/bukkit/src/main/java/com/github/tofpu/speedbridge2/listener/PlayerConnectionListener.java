package com.github.tofpu.speedbridge2.event;

import com.github.tofpu.speedbridge2.PlayerAdapter;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final PlayerAdapter playerAdapter;
    private final EventDispatcherService dispatcherService;

    public PlayerConnectionListener(PlayerAdapter playerAdapter,
        EventDispatcherService dispatcherService) {
        this.playerAdapter = playerAdapter;
        this.dispatcherService = dispatcherService;
    }

    @EventHandler
    public void on(final PlayerJoinEvent event) {
        OnlinePlayer player = this.playerAdapter.provideOnlinePlayer(
            event.getPlayer().getUniqueId());
        dispatcherService.dispatch(
            new com.github.tofpu.speedbridge2.event.event.PlayerJoinEvent(player));
    }

    @EventHandler
    public void on(final PlayerQuitEvent event) {
        OnlinePlayer player = this.playerAdapter.provideOnlinePlayer(
            event.getPlayer().getUniqueId());
        dispatcherService.dispatch(
            new com.github.tofpu.speedbridge2.event.event.PlayerLeaveEvent(player));
    }
}