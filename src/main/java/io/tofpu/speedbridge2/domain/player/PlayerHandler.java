package io.tofpu.speedbridge2.domain.player;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.island.setup.IslandSetupManager;
import io.tofpu.speedbridge2.domain.player.loader.PlayerLoader;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class PlayerHandler {
    private final @NotNull LoadingCache<UUID, BridgePlayer> playerMap;

    public PlayerHandler() {
        this.playerMap = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(PlayerLoader.INSTANCE);
    }

    /**
     * It returns a CompletableFuture that will eventually return the player with the given
     * uniqueId
     *
     * @param uniqueId The unique ID of the player.
     * @return A CompletableFuture<BridgePlayer>
     */
    public CompletableFuture<BridgePlayer> load(final UUID uniqueId) {
        return PluginExecutor.supply(() -> {
            // for loading purposes
            return this.playerMap.getUnchecked(uniqueId);
        });
    }

    /**
     * Returns the player with the given unique ID, or null if no such player exists
     *
     * @param uniqueId The unique ID of the player.
     * @return A BridgePlayer object.
     */
    public @Nullable BridgePlayer get(final UUID uniqueId) {
        return this.playerMap.asMap().get(uniqueId);
    }

    /**
     * Remove a player from the player map
     *
     * @param uniqueId The unique ID of the player to remove.
     * @return The BridgePlayer object that was removed from the map.
     */
    public @Nullable BridgePlayer remove(final UUID uniqueId) {
        return this.playerMap.asMap().remove(uniqueId);
    }

    /**
     * If the player is in the
     * database, update the name and refresh the player
     *
     * @param name The name of the player.
     * @param uniqueId The unique ID of the player.
     * @return A BridgePlayer object.
     */
    public @Nullable BridgePlayer internalRefresh(final String name,
            final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        if (bridgePlayer == null) {
            load(uniqueId);
            return null;
        }

        if (!bridgePlayer.getName().equals(name)) {
            Databases.PLAYER_DATABASE.updateName(name, bridgePlayer);
        }

        bridgePlayer.internalRefresh(uniqueId);

        return bridgePlayer;
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

        IslandSetupManager.INSTANCE.invalidate(uniqueId);

        return bridgePlayer;
    }

    /**
     * Returns a collection of all the players in the game
     *
     * @return A collection of all the players in the game.
     */
    public Collection<BridgePlayer> getBridgePlayers() {
        return Collections.unmodifiableCollection(playerMap.asMap().values());
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
