package com.github.tofpu.speedbridge2.bridge.score;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Scores {
    private final PlayerIdSlot id;
    private final LinkedList<Score> scoresList = new LinkedList<>();

    public Scores(PlayerIdSlot id) {
        this.id = id;
    }

    public List<Score> scoresList() {
        return Collections.unmodifiableList(scoresList);
    }

    public void add(Score score) {
        if (id.getIslandSlot() != score.getIslandSlot()) {
            throw new RuntimeException("Score's island does not match: input=" + score.getIslandSlot() + ", desired=" + id.getIslandSlot());
        }

        if (scoresList.isEmpty() || score.compareTo(scoresList.getLast()) > 0) {
            scoresList.add(score);
        }

        sortScores();
    }

    public Score get(int index) {
        return scoresList.get(index);
    }

    public int size() {
        return scoresList.size();
    }

    @SuppressWarnings("unchecked")
    private void sortScores() {
        LinkedList<Score> clone = (LinkedList<Score>) scoresList.clone();
        clone.sort(Score::compareTo);

        this.scoresList.clear();
        this.scoresList.addAll(clone);
    }
}
