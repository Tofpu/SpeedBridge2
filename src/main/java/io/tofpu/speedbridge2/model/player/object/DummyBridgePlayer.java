package io.tofpu.speedbridge2.model.player.object;

import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.leaderboard.Leaderboard;

import java.util.UUID;

public class DummyBridgePlayer extends BridgePlayer {
    public static DummyBridgePlayer of(final IslandService islandService,
            final Leaderboard leaderboard, final UUID uniqueId) {
        return new DummyBridgePlayer(islandService, leaderboard, uniqueId);
    }

    private DummyBridgePlayer(final IslandService islandService,
            final Leaderboard leaderboard, final UUID uniqueId) {
        super(islandService, leaderboard, uniqueId);
    }
}
