package io.tofpu.speedbridge2.score.domain;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface ScoreRegistry {
    void addScore(Score score);
    Optional<Score> bestScore();
    Optional<Score> bestScore(int slot);

    boolean remove(Score score);
    int remove(int slot, Predicate<Score> removalTest, int removalCount);
    Score removeLastScore();

    boolean isEmpty();

    @Unmodifiable
    Collection<Score> filterByIsland(int slot);
    @Unmodifiable
    Collection<Score> all();
}
