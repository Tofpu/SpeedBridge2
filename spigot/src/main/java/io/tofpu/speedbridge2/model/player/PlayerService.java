package io.tofpu.speedbridge2.model.player;

import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class PlayerService {
    private final @NotNull PlayerHandler playerHandler;

    public PlayerService() {
        this.playerHandler = new PlayerHandler();
    }

    /**
     * It returns a CompletableFuture that will eventually return the player with the given
     * uniqueId
     *
     * @param uid The unique ID of the player.
     * @return A CompletableFuture<BridgePlayer>
     */
    public CompletableFuture<BridgePlayer> loadAsync(final UUID uid) {
        return playerHandler.loadAsync(uid);
    }

    /**
     * Returns the player with the given unique ID, or null if no such player exists
     *
     * @param uuid The unique ID of the player.
     * @return A BridgePlayer object.
     */
    public @Nullable BridgePlayer getIfPresent(final @NotNull UUID uuid) {
        return this.playerHandler.getIfPresent(uuid);
    }

    /**
     * Returns the player with the given unique ID, or get a dummy object
     *
     * @param uuid The unique ID of the player.
     * @return A BridgePlayer object.
     */
    public @NotNull BridgePlayer getOrDefault(final @NotNull UUID uuid) {
        return this.playerHandler.getOrDefault(uuid);
    }

    /**
     * Remove a player from the player map if present
     *
     * @param uniqueId The unique ID of the player to remove.
     */
    public void remove(final @NotNull UUID uniqueId) {
        playerHandler.remove(uniqueId);
    }

    /**
     * If the player is in the
     * database, update the name and refresh the player instance
     *
     * @param player The player to refresh
     */
    public void internalRefresh(final @NotNull Player player) {
        playerHandler.internalRefresh(player.getName(), player.getUniqueId());
    }

    /**
     * If the player is online, update the name if it has changed and refresh the
     * player instance
     *
     * @param player       The live instance of the player.
     * @param bridgePlayer The bridge player instance that is being refreshed.
     */
    public void internalRefresh(final @NotNull Player player,
                                final @NotNull BridgePlayer bridgePlayer) {
        playerHandler.internalRefresh(player, bridgePlayer);
    }


    /**
     * This function invalidates a player by removing them from the bridge and the island
     * setup manager
     *
     * @param player The player to invalidate
     * @return The bridge player that was invalidated.
     */
    public @Nullable BridgePlayer invalidate(final @NotNull Player player) {
        return playerHandler.invalidate(player.getUniqueId());
    }

    /**
     * Returns a collection of all the players in the game
     *
     * @return A collection of all the players in the game.
     */
    public Collection<BridgePlayer> getBridgePlayers() {
        return playerHandler.getBridgePlayers();
    }

    /**
     * Resets all the player's data
     *
     * @param uuid The UUID of the player to reset.
     */
    public void reset(final UUID uuid) {
        reset(uuid, ResetType.ALL);
    }

    public @NotNull CompletableFuture<Void> reset(final UUID uuid, ResetType resetType) {
        return playerHandler.reset(uuid, resetType);
    }

    public void shutdown() {
        playerHandler.shutdown();
    }

    public void loadIfAbsent(final Player player,
                             final Consumer<BridgePlayer> notAbsentConsumer) {
        playerHandler.loadIfAbsent(player.getUniqueId(), notAbsentConsumer);
    }
}
