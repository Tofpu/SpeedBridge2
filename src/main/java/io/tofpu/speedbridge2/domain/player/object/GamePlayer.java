package io.tofpu.speedbridge2.domain.player.object;

import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public final class GamePlayer {
    private static final Map<UUID, GamePlayer> GAME_PLAYER_MAP = new HashMap<>();
    private final BridgePlayer player;
    private final List<Location> blockLocations;

    private boolean queue = false;
    private GameIsland currentGame = null;
    private long timer = -1;

    public static GamePlayer of(final BridgePlayer player) {
        return GAME_PLAYER_MAP.computeIfAbsent(player.getPlayerUid(),
                uuid -> new GamePlayer(player));
    }

    private GamePlayer(final BridgePlayer player) {
        this.player = player;
        this.blockLocations = new ArrayList<>();
    }

    public void setCurrentGame(final GameIsland gameIsland) {
        this.currentGame = gameIsland;
    }

    public void addBlock(final Block block) {
        this.blockLocations.add(block.getLocation());
    }

    public void removeBlock(final Block block) {
        this.blockLocations.add(block.getLocation());
    }

    public void resetBlocks() {
        for (final Location blockLocation : this.blockLocations) {
            blockLocation.getBlock().setType(Material.AIR);
        }
    }

    public void startQueue() {
        this.queue = true;
    }

    public void resetQueue() {
        this.queue = false;
    }

    public boolean isInQueue() {
        return queue;
    }

    public GameIsland getCurrentGame() {
        return currentGame;
    }

    public BridgePlayer getBridgePlayer() {
        return player;
    }

    public void teleport(final IslandPlot selectedPlot) {
        if (player == null) {
            return;
        }

        player.getPlayer()
                .teleport(selectedPlot.getIslandLocation());
    }

    public boolean hasPlaced(final Block block) {
        return this.blockLocations.contains(block.getLocation());
    }

    public long startTimer() {
        return this.timer = System.nanoTime();
    }

    public long getTimer() {
        return this.timer;
    }

    public void resetTimer() {
        this.timer = -1;
    }

    public boolean hasTimerStarted() {
        return this.timer != -1;
    }

    public GamePlayer remove() {
        GAME_PLAYER_MAP.remove(this.getBridgePlayer().getPlayerUid());
        return this;
    }
}
