package io.tofpu.speedbridge2.model.player.object.score;

import org.jetbrains.annotations.NotNull;

public final class Score implements Comparable<Score> {
    private final int scoredOn;
    private final double score;

    /**
     * Create a new Score object with the given island slot and score
     *
     * @param islandSlot The island slot that the score is for.
     * @param score The score of the island.
     * @return A new instance of Score.
     */
    public static Score of(final int islandSlot, final double score) {
        return new Score(islandSlot, score);
    }

    private Score(final int scoredOn, final double score) {
        this.scoredOn = scoredOn;
        this.score = score;
    }

    /**
     * Returns the score of the game
     *
     * @return The score of the player.
     */
    public double getScore() {
        return score;
    }

    /**
     * Returns the number of times the player has scored on the opponent
     *
     * @return The number of times the player scored on the opponent.
     */
    public int getScoredOn() {
        return scoredOn;
    }

    @Override
    public int compareTo(final @NotNull Score o) {
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
