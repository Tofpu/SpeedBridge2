package io.tofpu.speedbridge2.model.player.loader;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Expiry;
import io.tofpu.speedbridge2.model.common.PluginExecutor;
import io.tofpu.speedbridge2.model.common.database.Databases;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.leaderboard.meta.BoardRetrieve;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class PlayerLoader implements BoardRetrieve<BridgePlayer>, CacheLoader<UUID, BridgePlayer> {
    public static final PlayerLoader INSTANCE = new PlayerLoader();

    private PlayerLoader() {
        // prevent instantiation
    }

    @Override
    public BridgePlayer load(final @NotNull UUID key) throws Exception {
        return this.retrieve(key);
    }

    @Override
    public CompletableFuture<? extends BridgePlayer> asyncLoad(final UUID key, final Executor executor) {
        return retrieveAsync(key, executor);
    }

    @Override
    public BridgePlayer retrieve(final @NotNull UUID uniqueId) {
        BridgePlayer bridgePlayer;

        try {
            BridgeUtil.debug("PlayerLoader#retrieve: Attempting to load " + uniqueId +
                             " player data!");

            bridgePlayer = retrieveAsync(uniqueId, PluginExecutor.INSTANCE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

            // recovering from the exception
            bridgePlayer = BridgePlayer.of(uniqueId);
        }
        return bridgePlayer;
    }

    @Override
    public CompletableFuture<BridgePlayer> retrieveAsync(final @NotNull UUID uniqueId,
            final @NotNull Executor executor) {
        BridgeUtil.debug("attempting to load " + uniqueId + " player's data!");
        return Databases.PLAYER_DATABASE.getStoredPlayer(uniqueId);
    }

    public static final class PlayerRemovalListener implements Expiry<UUID, BridgePlayer> {
        private static final long EXPIRY_DURATION = TimeUnit.MINUTES.toNanos(5);
        private static final long INFINITE_DURATION = Long.MAX_VALUE;

        public static final PlayerRemovalListener INSTANCE = new PlayerRemovalListener();

        private PlayerRemovalListener() {
            // prevent instantiation
        }

        @Override
        public long expireAfterCreate(final UUID key, final BridgePlayer value, final long currentTime) {
            return EXPIRY_DURATION;
        }

        @Override
        public long expireAfterUpdate(final UUID key, final BridgePlayer value, final long currentTime,
                @NonNegative final long currentDuration) {
            final long elapse = Duration.ofNanos(currentDuration)
                    .getSeconds();
            BridgeUtil.debug("PlayerRemovalListener#expireAfterUpdate: Start: current " +
                             "duration is " + elapse + " seconds!");
            if (value.getPlayer() == null) {
                BridgeUtil.debug("PlayerRemovalListener#expireAfterUpdate: Expiring " + key + " player data after " + elapse + " seconds!");
                return EXPIRY_DURATION;
            }
            BridgeUtil.debug("PlayerRemovalListener#expireAfterUpdate: Not expiring " + key + " player data yet!");
            return INFINITE_DURATION;
        }

        @Override
        public long expireAfterRead(final UUID key, final BridgePlayer value, final long currentTime,
                @NonNegative final long currentDuration) {
            final long elapse = Duration.ofNanos(currentDuration)
                    .getSeconds();
            BridgeUtil.debug("PlayerRemovalListener#expireAfterRead: Start: " + elapse);
            if (value.getPlayer() != null) {
                BridgeUtil.debug("PlayerRemovalListener#expireAfterRead: Not expiring " + key + " player data yet!");
                return INFINITE_DURATION;
            }
            BridgeUtil.debug("PlayerRemovalListener#expireAfterRead: Expiring " + key + " player data after " + elapse + " seconds!");
            return currentDuration;
        }
    }
}
