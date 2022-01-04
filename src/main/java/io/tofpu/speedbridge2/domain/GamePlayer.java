package io.tofpu.speedbridge2.domain;

import org.bukkit.entity.Player;

public class GamePlayer {
    private final Player player;

    public GamePlayer(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
