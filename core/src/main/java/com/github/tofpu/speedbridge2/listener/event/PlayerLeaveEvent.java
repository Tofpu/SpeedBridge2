package com.github.tofpu.speedbridge2.listener.event;

import com.github.tofpu.speedbridge2.listener.Event;
import com.github.tofpu.speedbridge2.player.OnlinePlayer;

public class PlayerLeaveEvent extends Event {

    private final OnlinePlayer player;

    public PlayerLeaveEvent(OnlinePlayer player) {
        this.player = player;
    }

    public OnlinePlayer getPlayer() {
        return player;
    }
}
