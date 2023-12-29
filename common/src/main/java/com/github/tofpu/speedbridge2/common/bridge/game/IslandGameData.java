package com.github.tofpu.speedbridge2.common.bridge.game;

import com.github.tofpu.speedbridge2.common.game.land.Land;
import com.github.tofpu.speedbridge2.common.game.GameData;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.object.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class IslandGameData extends GameData {
    private final IslandGamePlayer player;
    private final Island island;
    private final Land land;

    private final List<Position> blockPositions = new ArrayList<>();

    private long timerInNanoTime = Integer.MIN_VALUE;

    public IslandGameData(IslandGamePlayer player, Island island, Land land) {
        this.player = player;
        this.island = island;
        this.land = land;
    }

    public IslandGamePlayer gamePlayer() {
        return player;
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
            gamePlayer().getPlayer().sendMessage("Timer has begun!");
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

    public double timerInSeconds() {
        return (double) (System.nanoTime() - timerInNanoTime) / 1_000_000_000;
    }
}
