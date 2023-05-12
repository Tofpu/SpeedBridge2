package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.player.OnlinePlayer;

import java.util.UUID;

public interface PlayerAdapter {
    OnlinePlayer provideOnlinePlayer(final UUID id);
    boolean isOnline(final UUID id);
}
