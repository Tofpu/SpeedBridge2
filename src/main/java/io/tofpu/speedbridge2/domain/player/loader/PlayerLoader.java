package io.tofpu.speedbridge2.domain.player.loader;

import com.google.common.cache.CacheLoader;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.leaderboard.meta.BoardRetrieve;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public final class PlayerLoader extends CacheLoader<UUID, BridgePlayer> implements BoardRetrieve<BridgePlayer> {
    public static final PlayerLoader INSTANCE = new PlayerLoader();

    @Override
    public BridgePlayer load(final @NotNull UUID key) throws Exception {
        return retrieve(key);
    }

    @Override
    public BridgePlayer retrieve(final @NotNull UUID uniqueId) {
        BridgePlayer bridgePlayer;

        try {
            BridgeUtil.debug("attempting to load " + uniqueId + " player's data!");

            bridgePlayer = Databases.PLAYER_DATABASE.getStoredPlayer(uniqueId)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

            // recovering from the exception
            bridgePlayer = BridgePlayer.of(uniqueId);
        }
        return bridgePlayer;
    }
}
