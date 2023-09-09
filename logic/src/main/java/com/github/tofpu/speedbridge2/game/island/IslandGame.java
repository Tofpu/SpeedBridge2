package com.github.tofpu.speedbridge2.game.island;

import com.github.tofpu.speedbridge2.game.core.Game;
import com.github.tofpu.speedbridge2.game.core.GamePlayer;
import com.github.tofpu.speedbridge2.game.island.arena.Land;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class IslandGame extends Game {
    private final Island island;
    private final Land land;

    public IslandGame(GamePlayer gamePlayer, Island island, Land land) {
        super(gamePlayer);
        this.island = island;
        this.land = land;
    }
}
