package com.github.tofpu.speedbridge2.bukkit.listener;

import com.github.tofpu.speedbridge2.PlatformPlayerAdapter;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final PlatformPlayerAdapter playerAdapter;
    private final EventDispatcherService dispatcherService;

    public PlayerConnectionListener(PlatformPlayerAdapter playerAdapter,
                                    EventDispatcherService dispatcherService) {
        this.playerAdapter = playerAdapter;
        this.dispatcherService = dispatcherService;
    }

    @EventHandler
    public void on(final PlayerJoinEvent event) {
        OnlinePlayer player = this.playerAdapter.provideOnlinePlayer(
            event.getPlayer().getUniqueId());
        dispatcherService.dispatchIfApplicable(
            new com.github.tofpu.speedbridge2.event.event.PlayerJoinEvent(player));
    }

    @EventHandler
    public void on(final PlayerQuitEvent event) {
        OnlinePlayer player = this.playerAdapter.provideOnlinePlayer(
            event.getPlayer().getUniqueId());
        dispatcherService.dispatchIfApplicable(
            new com.github.tofpu.speedbridge2.event.event.PlayerLeaveEvent(player));
    }
}