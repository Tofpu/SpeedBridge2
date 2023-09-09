package com.github.tofpu.speedbridge2.game.island;

import com.github.tofpu.speedbridge2.game.core.GamePlayer;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class IslandGamePlayer extends GamePlayer {
    private final OnlinePlayer player;
//    private final int slot;

    public IslandGamePlayer(OnlinePlayer player) {
        super(player.id());
        this.player = player;
//        this.slot = slot;
    }

//    public int slot() {
//        return slot;
//    }
}
