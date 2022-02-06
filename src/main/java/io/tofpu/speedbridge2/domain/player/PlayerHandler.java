package io.tofpu.speedbridge2.domain.player;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.player.loader.PlayerLoader;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
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

    public CompletableFuture<BridgePlayer> load(final UUID uniqueId) {
        return PluginExecutor.supply(() -> {
            // for loading purposes
            return this.playerMap.getUnchecked(uniqueId);
        });
    }

    public @Nullable BridgePlayer get(final UUID uniqueId) {
        return this.playerMap.asMap().get(uniqueId);
    }

    public @Nullable BridgePlayer remove(final UUID uniqueId) {
        return this.playerMap.asMap().remove(uniqueId);
    }

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

    public @Nullable BridgePlayer invalidate(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        if (bridgePlayer == null) {
            return null;
        }
        bridgePlayer.invalidatePlayer();

        return bridgePlayer;
    }

    public Collection<BridgePlayer> getBridgePlayers() {
        return Collections.unmodifiableCollection(playerMap.asMap().values());
    }
}
