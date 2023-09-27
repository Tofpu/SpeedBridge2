package com.github.tofpu.speedbridge2.bridge.game;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GamePlayer;
import com.github.tofpu.speedbridge2.bridge.Land;
import com.github.tofpu.speedbridge2.object.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class IslandGame extends Game {
    private final Island island;
    private final Land land;

    private final List<Position> blockPositions = new ArrayList<>();

    private long timerInNanoTime = Integer.MIN_VALUE;

    public IslandGame(GamePlayer gamePlayer, Island island, Land land) {
        super(gamePlayer);
        this.island = island;
        this.land = land;
    }

    public void resetState() {
        timerInNanoTime = Integer.MIN_VALUE;
        blockPositions.clear();
    }

    public boolean hasTimerBegun() {
        return timerInNanoTime != Integer.MIN_VALUE;
    }

    public void beginTimer(boolean notifyPlayer) {
        timerInNanoTime = System.nanoTime();

        if (notifyPlayer) {
            player().getPlayer().sendMessage("Timer has begun!");
        }
    }

    public void addBlock(Position position) {
        this.blockPositions.add(position);
    }

    public void removeBlock(Position position) {
        this.blockPositions.remove(position);
    }

    public boolean hasPlacedBlockAt(Position position) {
        return this.blockPositions.contains(position);
    }

    public List<Position> blockPlacements() {
        return Collections.unmodifiableList(blockPositions);
    }

    public IslandGamePlayer player() {
        return (IslandGamePlayer) gamePlayer();
    }

    public double timerInSeconds() {
        return (double) (System.nanoTime() - timerInNanoTime) / 1_000_000_000;
    }
}
