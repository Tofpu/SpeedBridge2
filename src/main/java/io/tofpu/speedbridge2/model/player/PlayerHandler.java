package io.tofpu.speedbridge2.model.player;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.tofpu.speedbridge2.model.common.database.Databases;
import io.tofpu.speedbridge2.model.island.object.setup.IslandSetupHandler;
import io.tofpu.speedbridge2.model.player.loader.PlayerLoader;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class PlayerHandler {
    private final @NotNull AsyncLoadingCache<UUID, BridgePlayer> playerMap;

    public PlayerHandler() {
        this.playerMap = Caffeine.newBuilder()
                .expireAfter(PlayerLoader.PlayerRemovalListener.INSTANCE)
                .buildAsync(PlayerLoader.INSTANCE);
    }

    /**
     * It returns a CompletableFuture that will eventually return the player with the given
     * uniqueId
     *
     * @param uniqueId The unique ID of the player.
     * @return A CompletableFuture<BridgePlayer>
     */
    public CompletableFuture<BridgePlayer> loadAsync(final UUID uniqueId) {
        return this.playerMap.get(uniqueId);
    }

    /**
     * Returns the player with the given unique ID, or null if no such player exists
     *
     * @param uniqueId The unique ID of the player.
     * @return A BridgePlayer object.
     */
    public @Nullable BridgePlayer getIfPresent(final UUID uniqueId) {
        return this.playerMap.synchronous().getIfPresent(uniqueId);
    }

    /**
     * Returns the player with the given unique ID, or get a dummy object
     *
     * @param uniqueId The unique ID of the player.
     * @return A BridgePlayer object.
     */
    public @NotNull BridgePlayer getOrDefault(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = getIfPresent(uniqueId);
        if (bridgePlayer == null) {
            return PlayerFactory.createDummy(uniqueId);
        }
        return bridgePlayer;
    }

    /**
     * Remove a player from the player map if present
     *
     * @param uniqueId The unique ID of the player to remove.
     */
    public void remove(final UUID uniqueId) {
        if (uniqueId == null) {
            return;
        }
        this.playerMap.synchronous().invalidate(uniqueId);
    }

    /**
     * If the player is in the
     * database, update the name and refresh the player instance
     *
     * @param name The name of the player.
     * @param uniqueId The unique ID of the player.
     */
    public void internalRefresh(final String name,
            final UUID uniqueId) {
        final BridgePlayer bridgePlayer = getIfPresent(uniqueId);
        if (bridgePlayer == null) {
            loadAsync(uniqueId);
            return;
        }

        if (!bridgePlayer.getName().equals(name)) {
            Databases.PLAYER_DATABASE.updateName(name, bridgePlayer);
        }

        bridgePlayer.internalRefresh(uniqueId);
    }

    /**
     * If the player is online, update the name if it has changed and refresh the
     * player instance
     *
     * @param player The live instance of the player.
     * @param bridgePlayer The bridge player instance that is being refreshed.
     */
    public void internalRefresh(final @NotNull Player player,
            final @NotNull BridgePlayer bridgePlayer) {
        final String name = player.getName();
        if (!bridgePlayer.getName()
                .equals(name)) {
            Databases.PLAYER_DATABASE.updateName(name, bridgePlayer);
        }

        final UUID uniqueId = player.getUniqueId();
        bridgePlayer.internalRefresh(uniqueId);
        playerMap.asMap()
                .compute(uniqueId, (uuid, bridgePlayerCompletableFuture) -> CompletableFuture.completedFuture(bridgePlayer));
    }

    /**
     * This function invalidates a player by removing them from the bridge and the island
     * setup manager
     *
     * @param uniqueId The unique ID of the player to invalidate.
     * @return The bridge player that was invalidated.
     */
    public @Nullable BridgePlayer invalidate(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = getIfPresent(uniqueId);
        if (bridgePlayer == null) {
            return null;
        }
        bridgePlayer.invalidatePlayer();
        playerMap.asMap().compute(uniqueId, (uuid, bridgePlayerCompletableFuture) -> CompletableFuture.completedFuture(bridgePlayer));

        IslandSetupHandler.INSTANCE.invalidate(uniqueId);

        return bridgePlayer;
    }

    /**
     * Returns a collection of all the players in the game
     *
     * @return A collection of all the players in the game.
     */
    public Collection<BridgePlayer> getBridgePlayers() {
        return Collections.unmodifiableCollection(playerMap.synchronous().asMap()
                .values());
    }

    /**
     * Resets the player's data
     *
     * @param uuid The UUID of the player to reset.
     */
    public void reset(final UUID uuid) {
        final BridgePlayer bridgePlayer = getIfPresent(uuid);
        if (bridgePlayer == null) {
            return;
        }
        bridgePlayer.reset();
    }

    /**
     * This function resets the blocks of all the players in the bridge game
     */
    public void shutdown() {
        for (final BridgePlayer bridgePlayer : getBridgePlayers()) {
            final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();
            if (gamePlayer == null) {
                return;
            }
            gamePlayer.resetBlocks();
        }
    }

    public void loadIfAbsent(final UUID uniqueId, final Consumer<BridgePlayer> notAbsentConsumer) {
        final BridgePlayer bridgePlayer = getIfPresent(uniqueId);
        if (bridgePlayer != null) {
            notAbsentConsumer.accept(bridgePlayer);
            return;
        }

        loadAsync(uniqueId);
    }
}
