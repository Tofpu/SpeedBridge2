package com.github.tofpu.speedbridge2.event.event;

import com.github.tofpu.speedbridge2.event.Event;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

public class PlayerJoinEvent extends Event {

    private final OnlinePlayer player;

    public PlayerJoinEvent(OnlinePlayer player) {
        this.player = player;
    }

    public OnlinePlayer getPlayer() {
        return player;
    }
}
