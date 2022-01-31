package io.tofpu.speedbridge2.domain.leaderboard.wrapper;

import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

public final class GlobalBoardPlayer {
    private final int position;
    private final BridgePlayer bridgePlayer;

    public GlobalBoardPlayer(final int position, final BridgePlayer bridgePlayer) {
        this.position = position;
        this.bridgePlayer = bridgePlayer;
    }

    public int getPosition() {
        return position;
    }

    public BridgePlayer getBridgePlayer() {
        return bridgePlayer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BoardPlayer{");
        sb.append("position=")
                .append(position);
        sb.append(", bridgePlayer=")
                .append(bridgePlayer);
        sb.append('}');
        return sb.toString();
    }
}
