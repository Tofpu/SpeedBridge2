package io.tofpu.speedbridge2.domain.leaderboard.wrapper;

import io.tofpu.speedbridge2.domain.player.misc.Score;

import java.util.UUID;

public final class BoardPlayer {
    private final int position;
    private final UUID owner;
    private final Score score;

    public BoardPlayer(final int position, final UUID owner, final Score score) {
        this.position = position;
        this.owner = owner;
        this.score = score;
    }

    public int getPosition() {
        return position;
    }

    public UUID getOwner() {
        return owner;
    }

    public Score getScore() {
        return score;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BoardPlayer{");
        sb.append("position=")
                .append(position);
        sb.append(", score=")
                .append(score);
        sb.append('}');
        return sb.toString();
    }
}
