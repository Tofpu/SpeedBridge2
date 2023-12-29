package com.github.tofpu.speedbridge2.common.bridge.game.score;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import org.hibernate.Session;

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

    public CompletableFuture<?> storeScore(Score score, boolean removeOldScores) {
        return database.executeAsync(session -> {
            System.out.println("storeScore: " + score.seconds() + " (" + score + ")");
            if (removeOldScores) {
                removeOldScores(score, session);
            }
            session.persist(score);
        });
    }

    private void removeOldScores(Score score, Session session) {
        List<Score> targetList = session.createQuery("FROM scores WHERE playerId = ?1 AND nanoseconds >= ?2", Score.class)
                .setParameter(1, score.getPlayerId())
                .setParameter(2, score.nanoseconds())
                .list();
        assert targetList != null && !targetList.isEmpty() : "There is no target that is greater than " + score.seconds() + " seconds";

        Score target = targetList.stream().max(Score::compareTo).get();
        assertGreatestTargetChoice(targetList, target);

        session.remove(target);
        System.out.println("Removed: " + targetList + " (" + target.seconds() + ")");
    }

    private void assertGreatestTargetChoice(List<Score> targetList, Score target) {
        Score greatestTarget = null;
        for (Score value : targetList) {
            if (value.nanoseconds() > target.nanoseconds()) {
                greatestTarget = value;
            }
        }
        assert greatestTarget == null : "Found a greater score than originally chosen: original=" + target.seconds() + ", expected=" + greatestTarget.seconds();
    }
}
