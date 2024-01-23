package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.gameextra.GamePlayer;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class IslandGamePlayer extends GamePlayer {
    private final OnlinePlayer player;

    public IslandGamePlayer(OnlinePlayer player) {
        super(player.id());
        this.player = player;
    }
}
