package io.tofpu.speedbridge2.score.service;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.ScoreRegistry;
import io.tofpu.speedbridge2.score.domain.event.ScoreRegisteredEvent;
import io.tofpu.speedbridge2.score.domain.event.ScoreUnregisteredEvent;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedSet;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ScoreRegistryImpl implements ScoreRegistry {
    private final EventBus eventBus;
    private final UUID playerId;
    private final SortedSet<Score> scores;

    public ScoreRegistryImpl(EventBus eventBus, UUID playerId, SortedSet<Score> scores) {
        this.eventBus = eventBus;
        this.playerId = playerId;
        this.scores = scores;
    }

    @Override
    public void addScore(Score score) {
        if (score.playerId().equals(playerId)) {
            scores.add(score);
            eventBus.post(ScoreRegisteredEvent.class, score);
        } else {
            throw new IllegalArgumentException("Score does not belong to the player");
        }
    }

    @Override
    public Optional<Score> bestScore(int slot) {
        return scores.stream()
                .filter(score -> score.slot() == slot)
                .min(Score::compareTo)
                .or(Optional::empty);
    }

    @Override
    public boolean remove(Score score) {
        if (scores.remove(score)) {
            eventBus.post(ScoreUnregisteredEvent.class, score);
            return true;
        }
        return false;
    }

    @Override
    public int remove(int slot, Predicate<Score> removalTest, int removalCount) {
        Iterator<Score> iterator = scores.iterator();
        int totalRemoved = 0;
        while (iterator.hasNext()) {
            Score score = iterator.next();
            if (score.slot() != slot) continue;
            if (!removalTest.test(score)) continue;

            iterator.remove();
            eventBus.post(ScoreUnregisteredEvent.class, score);

            totalRemoved++;
            removalCount--;
            if (removalCount == 0) break;
        }
        return totalRemoved;
    }

    @Override
    public Score removeLastScore() {
        if (scores.isEmpty()) {
            throw new IllegalArgumentException("Scores are empty");
        }
        Score last = scores.last();
        remove(last);
        return last;
    }

    @Override
    public boolean isEmpty() {
        return scores.isEmpty();
    }

    @Override
    public Collection<Score> filterByIsland(int slot) {
        return scores.stream()
                .filter(score -> score.slot() == slot)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    @Unmodifiable
    public Collection<Score> all() {
        return Collections.unmodifiableCollection(scores);
    }
}
