package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.game.arena.LandArea;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.stat.PlayerStatType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GamePlayer {
    private final BridgePlayer bridgePlayer;
    private final Player player;
    private final List<Location> blockLocations;

    private boolean queue = false;
    private GameSession currentGame = null;
    private long timer = -1;

    public GamePlayer(final BridgePlayer player) {
        this.bridgePlayer = player;
        this.player = player.getPlayer();
        this.blockLocations = new ArrayList<>();
    }

    public void setCurrentGame(final GameSession gameSession) {
        this.currentGame = gameSession;
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
        BridgeUtil.debug("GamePlayer(): Resetting the blocks now!");
        for (final Location blockLocation : this.blockLocations) {
            final Block block = blockLocation.getBlock();
            BridgeUtil.debug("GamePlayer(): Resetting " + block.getType() + " at " + block.getX() +
                    ", " + block.getY() + ", " + block.getZ() + " location!");
            block.setType(Material.AIR);
        }
        BridgeUtil.debug("GamePlayer(): Finished resetting the blocks! clearing the " +
                "block locations " +
                "immediately!");
        this.blockLocations.clear();
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
    public GameSession getCurrentGame() {
        return currentGame;
    }

    /**
     * Returns the player object associated with this bridge player
     *
     * @return The player object.
     */
    public Player getSpigotPlayer() {
        return player;
    }

    /**
     * It teleports the player to the selected plot
     *
     * @param landArea The plot that the player has selected.
     */
    public void teleport(final LandArea landArea) {
        teleport(landArea.getIslandLocation());
    }

    public void teleport(final Location location) {
        if (player == null) {
            return;
        }

        getSpigotPlayer().teleport(location);
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

    public Collection<Location> getPlacedBlocks() {
        return Collections.unmodifiableCollection(blockLocations);
    }

    public Inventory getInventory() {
        return player.getInventory();
    }

    public void setGameMode(GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public void setFoodLevel(int foodLevel) {
        player.setFoodLevel(foodLevel);
    }

    public double getMaxHealth() {
        return player.getMaxHealth();
    }

    public void setHealth(double maxHealth) {
        player.setHealth(maxHealth);
    }

    public Material getChoseMaterial() {
        return bridgePlayer.getChoseMaterial();
    }

    public void increment(PlayerStatType playerStatType) {
        bridgePlayer.increment(playerStatType);
    }
}
