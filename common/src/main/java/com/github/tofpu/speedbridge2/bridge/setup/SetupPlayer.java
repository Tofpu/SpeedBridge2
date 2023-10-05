package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.core.GamePlayer;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

public class SetupPlayer extends GamePlayer {
    private final OnlinePlayer player;

    public SetupPlayer(OnlinePlayer player) {
        super(player.id());
        this.player = player;
    }

    public OnlinePlayer player() {
        return player;
    }
}
