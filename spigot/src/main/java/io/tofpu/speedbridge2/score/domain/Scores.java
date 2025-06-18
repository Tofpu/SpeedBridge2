package io.tofpu.speedbridge2.score.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.SortedSet;
import java.util.UUID;
import java.util.stream.Collectors;

public record Scores(UUID playerId, SortedSet<Score> scores) {
    public void addScore(Score score) {
        if (score.playerId().equals(playerId)) {
            scores.add(score);
        } else {
            throw new IllegalArgumentException("Score does not belong to the player");
        }
    }

    public Optional<Score> bestScore(int slot) {
        return scores.stream()
                .filter(score -> score.slot() == slot)
                .min(Score::compareTo)
                .or(Optional::empty);
    }

    public Score removeLastScore() {
        if (scores.isEmpty()) {
            throw new IllegalArgumentException("Scores are empty");
        }
        Score last = scores.last();
        scores.remove(last);
        return last;
    }

    public boolean hasScore(int slot) {
        return scores.stream().anyMatch(score -> score.slot() == slot);
    }

    public boolean isEmpty() {
        return scores.isEmpty();
    }

    public Collection<Score> filterByIsland(int slot) {
        return scores.stream()
                .filter(score -> score.slot() == slot)
                .sorted()
                .collect(Collectors.toList());
    }

    public int size(int slot) {
        return Math.toIntExact(scores.stream()
                .filter(score -> score.slot() == slot)
                .count());
    }
}
