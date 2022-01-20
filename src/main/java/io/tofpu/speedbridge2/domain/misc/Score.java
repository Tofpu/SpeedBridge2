package io.tofpu.speedbridge2.domain.misc;

public final class Score implements Comparable<Score> {
    private final int scoredOn;
    private final double score;

    public static Score of(final int islandSlot, final double score) {
        return new Score(islandSlot, score);
    }

    public Score(final int scoredOn, final double score) {
        this.scoredOn = scoredOn;
        this.score = score;
    }

    public double getScore() {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Score{");
        sb.append("scoredOn=").append(scoredOn);
        sb.append(", score=").append(score);
        sb.append('}');
        return sb.toString();
    }
}
