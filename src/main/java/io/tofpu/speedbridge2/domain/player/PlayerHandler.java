package io.tofpu.speedbridge2.domain.player;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.player.loader.PlayerLoader;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class PlayerHandler {
    private final LoadingCache<UUID, BridgePlayer> playerMap;

    public PlayerHandler() {
        this.playerMap = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(PlayerLoader.INSTANCE);
    }

    public void load(final UUID uniqueId) {
        CompletableFuture.runAsync(() -> {
            // for loading purposes
            this.playerMap.getUnchecked(uniqueId);
        });
    }

    public BridgePlayer get(final UUID uniqueId) {
        return this.playerMap.asMap().get(uniqueId);
    }

    public BridgePlayer remove(final UUID uniqueId) {
        return this.playerMap.asMap().remove(uniqueId);
    }

    public BridgePlayer internalRefresh(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        if (bridgePlayer == null) {
            load(uniqueId);
            return null;
        }

        bridgePlayer.internalRefresh(uniqueId);

        return bridgePlayer;
    }

    public BridgePlayer invalidate(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        if (bridgePlayer == null) {
            return null;
        }
        bridgePlayer.invalidatePlayer();

        return bridgePlayer;
    }
}
