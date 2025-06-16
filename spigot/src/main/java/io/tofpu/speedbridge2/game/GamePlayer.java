package io.tofpu.speedbridge2.game;

import java.util.ArrayList;
import java.util.List;

import io.tofpu.speedbridge2.game.domain.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GamePlayer {
    private final Player player;
    private final Game game;

    private final List<Location> placedBlocks = new ArrayList<>();
    private long startTimer = -1;
    private long endTimer = -1;

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public boolean hasTimerStarted() {
        return startTimer != -1;
    }

    public void startTimer() {
        startTimer = System.currentTimeMillis();
    }

    public void endTimer() {
        endTimer = System.currentTimeMillis();
    }

    public long elapsedTimerInMillis() {
        return endTimer - startTimer;
    }

    public void clearTimer() {
        startTimer = -1;
        endTimer = -1;
    }

    public void addPlacedBlock(Location location) {
        placedBlocks.add(location);
    }

    public boolean removePlacedBlock(Location location) {
        return placedBlocks.remove(location);
    }

    // todo: add forEach, contains method instead
    public List<Location> placedBlocks() {
        return placedBlocks;
    }

    public Player player() {
        return player;
    }

    public Game game() {
        return game;
    }
}
