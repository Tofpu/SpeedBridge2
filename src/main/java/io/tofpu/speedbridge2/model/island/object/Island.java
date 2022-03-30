package io.tofpu.speedbridge2.model.island.object;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.database.Databases;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.leaderboard.LeaderboardMap;
import io.tofpu.speedbridge2.model.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.model.island.schematic.IslandSchematic;
import io.tofpu.speedbridge2.model.player.misc.score.Score;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Island {
    private final int slot;
    private final Map<GamePlayer, GameIsland> islandMap;
    private String category;

    private final LeaderboardMap leaderboardMap;
    private final IslandSchematic islandSchematic;
    private Location absoluteLocation;

    public Island(final int slot, final String category) {
        this.slot = slot;
        this.islandMap = new HashMap<>();
        this.category = category;

        this.leaderboardMap = new LeaderboardMap();
        this.islandSchematic = new IslandSchematic(this);
        this.absoluteLocation = null;
    }

    /**
     * Given a position, return the player at that position
     *
     * @param position The position of the player in the leaderboard.
     * @return A BoardPlayer object.
     */
    public BoardPlayer retrieveBy(final int position) {
        return leaderboardMap.get(position);
    }

    /**
     * This function will create a new GameIsland object and add it to the islandMap
     *
     * @param player The player who is joining the island.
     * @return A Map.Entry<GamePlayer, GameIsland>
     */
    public Map.Entry<GamePlayer, GameIsland> join(final BridgePlayer player) {
        // if a schematic cannot be found, return null
        if (islandSchematic.getSchematicClipboard() == null) {
            return null;
        }

        final GamePlayer gamePlayer = GamePlayer.of(player);
        final GameIsland gameIsland = GameIsland.of(this, gamePlayer);

        // starting the plot process
        gameIsland.start();

        this.islandMap.put(gamePlayer, gameIsland);
        return new AbstractMap.SimpleImmutableEntry<>(gamePlayer, gameIsland);
    }

    /**
     * When a player leaves the island, the island is removed from the island map and the
     * game player is removed from the game player map
     *
     * @param bridgePlayer The bridge player that is leaving the game island.
     */
    public void leaveGame(final BridgePlayer bridgePlayer) {
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();
        if (gamePlayer == null) {
            return;
        }

        final GameIsland gameIsland = this.islandMap.remove(gamePlayer);
        if (gameIsland == null) {
            return;
        }

        final Player player = bridgePlayer.getPlayer();
        if (player != null) {
            player.getInventory()
                    .clear();
        }

        BridgeUtil.sendMessage(player, String.format(Message.INSTANCE.leftAnIsland, slot));
        bridgePlayer.setGamePlayer(null);

        // reset the game island
        gameIsland.remove();
    }

    /**
     * Find the game island that the player is currently in
     *
     * @param gamePlayer The player who is trying to join the game.
     * @return The island that the player is on.
     */
    public GameIsland findGameByPlayer(final GamePlayer gamePlayer) {
        return this.islandMap.get(gamePlayer);
    }

    /**
     * It sets the category of the question.
     *
     * @param anotherCategory The new category to set.
     */
    public void setCategory(final String anotherCategory) {
        this.category = anotherCategory;
        update();
    }

    /**
     * Selects a schematic from the list of available schematics
     *
     * @param schematicName The name of the schematic to select.
     * @return A boolean value indicating whether the operation was successful.
     */
    public boolean selectSchematic(final @NotNull String schematicName) {
        final boolean successful = islandSchematic.selectSchematic(schematicName);
        // if the operation was successful, update the database
        if (successful) {
            update();
        }
        return successful;
    }

    /**
     * This function sets the absolute location of the object
     *
     * @param newAbsoluteLocation The new location to set the object to.
     */
    public void setAbsoluteLocation(final Location newAbsoluteLocation) {
        this.absoluteLocation = newAbsoluteLocation;
        update();
    }

    /**
     * Returns the absolute location of the object
     *
     * @return The absolute location of the object.
     */
    public Location getAbsoluteLocation() {
        return this.absoluteLocation;
    }

    /**
     * This function checks to see if the island schematic is ready to be used.
     *
     * @return A boolean value.
     */
    public boolean isReady() {
        return islandSchematic.getSchematicClipboard() != null &&
               absoluteLocation != null;
    }

    /**
     * This function updates the database with the current values of the object
     */
    protected void update() {
        Databases.ISLAND_DATABASE.update(this);
    }

    /**
     * @return The slot number of the island.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return The category of the island.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Load the leaderboard map with the given map
     *
     * @param boardMap The map to load into the leaderboard map.
     */
    public void loadBoard(final Map<Integer, BoardPlayer> boardMap) {
        this.leaderboardMap.load(boardMap);
    }

    /**
     * Add a score to the leaderboard for the given player
     *
     * @param bridgePlayer The bridge player to add the score to.
     * @param score The score to add.
     */
    public void addLeaderboardScore(final BridgePlayer bridgePlayer, final Score score) {
        leaderboardMap.append(bridgePlayer, score);
    }

    /**
     * Forces the leaderboard to be updated
     */
    public void updateLeaderboard() {
        this.leaderboardMap.updateLeaderboard();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Island{");
        sb.append("slot=")
                .append(slot);
        sb.append(", islandMap=")
                .append(islandMap);
        sb.append(", category='")
                .append(category)
                .append('\'');
        sb.append(", boardMap=")
                .append(leaderboardMap);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Reset the player's score to 0
     *
     * @param uuid The uuid of the player to reset.
     */
    public void resetPlayer(final UUID uuid) {
        this.leaderboardMap.reset(uuid);
    }

    /**
     * Returns the clipboard of the island schematic
     *
     * @return The Clipboard object.
     */
    public Clipboard getSchematicClipboard() {
        return islandSchematic.getSchematicClipboard();
    }

    /**
     * Returns the clipboard of the island schematic
     *
     * @return The Clipboard object.
     */
    public ClipboardWrapper getSchematicClipboardWrapper() {
        return islandSchematic.getSchematicClipboardWrapper();
    }

    /**
     * Returns the name of the schematic
     *
     * @return The name of the schematic.
     */
    public String getSchematicName() {
        return islandSchematic.getSchematicName();
    }

    /**
     * Delete the island from the database and memory
     */
    public void delete() {
        IslandService.INSTANCE.deleteIsland(getSlot());
    }
}