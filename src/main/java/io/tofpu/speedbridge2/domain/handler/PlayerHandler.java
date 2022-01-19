package io.tofpu.speedbridge2.domain.handler;

import io.tofpu.speedbridge2.database.Databases;
import io.tofpu.speedbridge2.domain.BridgePlayer;

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
}
