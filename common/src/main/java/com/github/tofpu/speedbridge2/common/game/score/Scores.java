package com.github.tofpu.speedbridge2.common.game.score;

import java.util.*;

public class Scores {
    public static final int MAXIMUM_SIZE = 5;
    private final PlayerIdSlot id;
    private final LinkedList<Score> scoresList = new LinkedList<>();

    Scores(PlayerIdSlot id) {
        this.id = id;
    }

    public List<Score> scoresList() {
        return Collections.unmodifiableList(scoresList);
    }

    public boolean add(Score score) {
        if (id.getIslandSlot() != score.getIslandSlot()) {
            throw new IllegalStateException("Score's island does not match: input=" + score.getIslandSlot() + ", desired=" + id.getIslandSlot());
        }

        scoresList.add(score);
        sortScores();

        return scoresList.contains(score);
    }

    public Score get(int index) {
        return scoresList.get(index);
    }

    public Score getBestScore() {
        return scoresList.getFirst();
    }

    public int size() {
        return scoresList.size();
    }

    @SuppressWarnings("unchecked")
    private void sortScores() {
        LinkedList<Score> clone = (LinkedList<Score>) scoresList.clone();
        clone.sort(Score::compareTo);

        this.scoresList.clear();
        this.scoresList.addAll(clone.subList(0, Math.min(clone.size(), MAXIMUM_SIZE)));
    }

    public UUID playerId() {
        return id.getPlayerId();
    }

    public int islandSlot() {
        return id.getIslandSlot();
    }
}
