package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

import java.util.UUID;

public interface PlatformPlayerAdapter {

    OnlinePlayer provideOnlinePlayer(final UUID id);

    boolean isOnline(final UUID id);
}
