package com.github.tofpu.speedbridge2.common.setup;

import com.github.tofpu.speedbridge2.common.gameextra.GamePlayer;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;

public class IslandSetupPlayer extends GamePlayer {
    private final OnlinePlayer player;

    public IslandSetupPlayer(OnlinePlayer player) {
        super(player.id());
        this.player = player;
    }

    public OnlinePlayer player() {
        return player;
    }
}
