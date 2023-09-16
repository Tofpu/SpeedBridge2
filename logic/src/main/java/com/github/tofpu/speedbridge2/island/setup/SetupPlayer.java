package com.github.tofpu.speedbridge2.island.setup;

import com.github.tofpu.speedbridge2.game.core.GamePlayer;
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
