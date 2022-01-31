package io.tofpu.speedbridge2.domain.leaderboard.wrapper;

import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

public final class BoardPlayer {
    private final int position;
    private final BridgePlayer bridgePlayer;

    public BoardPlayer(final int position, final BridgePlayer bridgePlayer) {
        this.position = position;
        this.bridgePlayer = bridgePlayer;
    }

    public int getPosition() {
        return position;
    }

    public BridgePlayer getBridgePlayer() {
        return bridgePlayer;
    }
}
