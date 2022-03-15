package io.tofpu.speedbridge2.domain.player.object.extra;

import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.UUID;

public class DummyBridgePlayer extends BridgePlayer {
    public static DummyBridgePlayer of(final UUID uniqueId) {
        return new DummyBridgePlayer(uniqueId);
    }

    private DummyBridgePlayer(final UUID uniqueId) {
        super(uniqueId);
    }
}
