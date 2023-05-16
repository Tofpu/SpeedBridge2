package com.github.tofpu.speedbridge2.listener.event;

import com.github.tofpu.speedbridge2.listener.Event;
import com.github.tofpu.speedbridge2.player.OnlinePlayer;

public class PlayerJoinEvent extends Event {

    private final OnlinePlayer player;

    public PlayerJoinEvent(OnlinePlayer player) {
        this.player = player;
    }

    public OnlinePlayer getPlayer() {
        return player;
    }
}
