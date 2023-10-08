package com.github.tofpu.speedbridge2.bridge.score;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ScoreRepository {
    private final DatabaseService database;

    public ScoreRepository(DatabaseService database) {
        this.database = database;
    }

    public CompletableFuture<List<Score>> loadScores(UUID playerId) {
        return database.computeAsync(session -> session.createQuery("FROM scores WHERE playerId = ?1", Score.class).setParameter(1, playerId)
                .list());
    }

    public CompletableFuture<?> storeScore(Score score) {
        return database.executeAsync(session -> session.persist(score));
    }
}
