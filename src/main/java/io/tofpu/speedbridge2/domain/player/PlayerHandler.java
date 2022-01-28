package io.tofpu.speedbridge2.domain.player;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerHandler {
    private final Map<UUID, BridgePlayer> playerMap = new HashMap<>();

    public void load(final Map<UUID, BridgePlayer> playerMap) {
        this.playerMap.putAll(playerMap);
    }

    public BridgePlayer get(final UUID playerUid) {
        return this.playerMap.computeIfAbsent(playerUid, uuid -> {
            final BridgePlayer bridgePlayer = BridgePlayer.of(uuid);
            Databases.PLAYER_DATABASE.insert(bridgePlayer);
            return bridgePlayer;
        });
    }

    public BridgePlayer remove(final UUID uniqueId) {
        return this.playerMap.remove(uniqueId);
    }

    public BridgePlayer internalRefresh(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        bridgePlayer.internalRefresh(uniqueId);

        return bridgePlayer;
    }

    public BridgePlayer invalidate(final UUID uniqueId) {
        final BridgePlayer bridgePlayer = get(uniqueId);
        bridgePlayer.invalidatePlayer();

        return bridgePlayer;
    }
}
