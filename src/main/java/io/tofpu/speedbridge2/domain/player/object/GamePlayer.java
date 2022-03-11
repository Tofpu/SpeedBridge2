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

    /**
     * It creates a new GamePlayer object for the given player.
     *
     * @param player The player that is being added to the game.
     * @return A GamePlayer object.
     */
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

    /**
     * Add a block to the list of blocks
     *
     * @param block The block that was just added to the world.
     */
    public void addBlock(final Block block) {
        this.blockLocations.add(block.getLocation());
    }

    /**
     * Add the block to the list of blocks that have been removed
     *
     * @param block The block that was removed.
     */
    public void removeBlock(final Block block) {
        this.blockLocations.add(block.getLocation());
    }

    /**
     * It iterates through the list of block locations and sets each block to air
     */
    public void resetBlocks() {
        for (final Location blockLocation : this.blockLocations) {
            blockLocation.getBlock().setType(Material.AIR);
        }
    }

    /**
     * Start the queue
     */
    public void startQueue() {
        this.queue = true;
    }

    /**
     * Reset the queue to false
     */
    public void resetQueue() {
        this.queue = false;
    }

    /**
     * Returns true if the process is in the queue, false otherwise
     *
     * @return A boolean value.
     */
    public boolean isInQueue() {
        return queue;
    }

    /**
     * Returns the current game
     *
     * @return The current game that is being played.
    */
    public GameIsland getCurrentGame() {
        return currentGame;
    }

     /**
     * Returns the player object associated with this bridge player
     *
     * @return The player object.
     */
    public BridgePlayer getBridgePlayer() {
        return player;
    }

    /**
     * It teleports the player to the selected plot
     *
     * @param selectedPlot The plot that the player has selected.
     */
    public void teleport(final IslandPlot selectedPlot) {
        if (player == null) {
            return;
        }

        player.getPlayer().teleport(selectedPlot.getIslandLocation());
    }

    /**
     * Returns true if the given block has been placed by the player
     *
     * @param block The block that was placed.
     * @return A boolean value.
     */
    public boolean hasPlaced(final Block block) {
        return this.blockLocations.contains(block.getLocation());
    }

    /**
     * Start a timer
     *
     * @return The current time in nanoseconds.
     */
    public long startTimer() {
        return this.timer = System.nanoTime();
    }

    /**
     * Get the timer value
     *
     * @return The timer value.
     */
    public long getTimer() {
        return this.timer;
    }

    /**
     * Reset the timer to -1
     */
    public void resetTimer() {
        this.timer = -1;
    }

    /**
     * Has the timer started?
     *
     * @return A boolean value.
     */
    public boolean hasTimerStarted() {
        return this.timer != -1;
    }

    /**
     * Remove the player from the game
     *
     * @return The GamePlayer object that was removed.
     */
    public GamePlayer remove() {
        resetBlocks();

        GAME_PLAYER_MAP.remove(this.getBridgePlayer().getPlayerUid());
        return this;
    }
}
