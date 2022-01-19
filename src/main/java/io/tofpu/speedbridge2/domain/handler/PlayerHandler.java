package io.tofpu.speedbridge2.domain.handler;

import io.tofpu.speedbridge2.domain.BridgePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerHandler {
    private final Map<UUID, BridgePlayer> playerMap = new HashMap<>();

    public BridgePlayer get(final UUID playerUid) {
        return this.playerMap.computeIfAbsent(playerUid, BridgePlayer::of);
    }
}
