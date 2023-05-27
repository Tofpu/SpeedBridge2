package com.github.tofpu.speedbridge2.event.event;

import com.github.tofpu.speedbridge2.event.Event;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

public class PlayerLeaveEvent extends Event {

    private final OnlinePlayer player;

    public PlayerLeaveEvent(OnlinePlayer player) {
        this.player = player;
    }

    public OnlinePlayer getPlayer() {
        return player;
    }
}
