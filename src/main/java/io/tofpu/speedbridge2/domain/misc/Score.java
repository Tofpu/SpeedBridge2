package io.tofpu.speedbridge2.domain.misc;

public final class Score implements Comparable<Score> {
    private final int scoredOn;
    private final long score;

    public static Score of(final int islandSlot, final long score) {
        return new Score(islandSlot, score);
    }

    public Score(final int scoredOn, final long score) {
        this.scoredOn = scoredOn;
        this.score = score;
    }

    public long getScore() {
        return score;
    }

    public int getScoredOn() {
        return scoredOn;
    }

    @Override
    public int compareTo(final Score o) {
        if (this.score > o.getScore()) {
            return 1;
        } else if (this.score == o.getScore()) {
            return 0;
        }
        return -1;
    }
}
