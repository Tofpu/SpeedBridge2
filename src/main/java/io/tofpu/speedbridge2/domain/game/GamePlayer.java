package io.tofpu.speedbridge2.domain.game;

import io.tofpu.speedbridge2.domain.schematic.IslandPlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public final class GamePlayer {
    private static final Map<UUID, GamePlayer> GAME_PLAYER_MAP = new HashMap<>();
    private final Player player;
    private final List<Location> blockLocations;

    private boolean queue = false;
    private GameIsland currentGame = null;

    public static GamePlayer of(final Player player) {
        return GAME_PLAYER_MAP.computeIfAbsent(player.getUniqueId(), uuid -> new GamePlayer(player));
    }

    private GamePlayer(final Player player) {
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

    public boolean isPlaying() {
        return currentGame != null;
    }

    public GameIsland getCurrentGame() {
        return currentGame;
    }

    public Player getPlayer() {
        return player;
    }

    public void teleport(final IslandPlot selectedPlot) {
        if (player == null) {
            return;
        }
        player.teleport(new Location(selectedPlot.getWorld(), selectedPlot.getX(), selectedPlot.getY(), selectedPlot.getZ()));
    }

    public boolean hasPlaced(final Block block) {
        return this.blockLocations.contains(block.getLocation());
    }

    public GamePlayer remove() {
        GAME_PLAYER_MAP.remove(this.getPlayer().getUniqueId());
        return this;
    }
}
