package io.tofpu.speedbridge2.model.player;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.tofpu.speedbridge2.model.common.database.Databases;
import io.tofpu.speedbridge2.model.island.setup.IslandSetupManager;
import io.tofpu.speedbridge2.model.player.loader.PlayerLoader;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.model.player.object.extra.DummyBridgePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    public @Nullable BridgePlayer get(final UUID uniqueId) {
        return this.playerMap.synchronous().getIfPresent(uniqueId);
    }

    /**
     * Returns the player with the given unique ID, or get a dummy object
     *
     * @param uniqueId The unique ID of the player.
     * @return A BridgePlayer object.
     */
    public @NotNull BridgePlayer getOrDefault(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        if (bridgePlayer == null) {
            return DummyBridgePlayer.of(uniqueId);
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
        final BridgePlayer bridgePlayer = get(uniqueId);
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
     * This function invalidates a player by removing them from the bridge and the island
     * setup manager
     *
     * @param uniqueId The unique ID of the player to invalidate.
     * @return The bridge player that was invalidated.
     */
    public @Nullable BridgePlayer invalidate(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        if (bridgePlayer == null) {
            return null;
        }
        bridgePlayer.invalidatePlayer();
        playerMap.asMap().compute(uniqueId, (uuid, bridgePlayerCompletableFuture) -> CompletableFuture.completedFuture(bridgePlayer));

        IslandSetupManager.INSTANCE.invalidate(uniqueId);

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
        final BridgePlayer bridgePlayer = get(uuid);
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
}
