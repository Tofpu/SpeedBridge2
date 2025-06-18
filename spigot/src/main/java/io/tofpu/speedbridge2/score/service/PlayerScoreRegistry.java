package io.tofpu.speedbridge2.score.service;

import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.Scores;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

public class PlayerScoreRegistry {
    private final Map<UUID, Scores> playerScores = new HashMap<>();

    public void register(Score score) {
        UUID playerId = score.playerId();
        playerScores.computeIfAbsent(playerId, PlayerScoreRegistry::createScores)
                        .addScore(score);
    }

    public Scores scores(UUID playerId) {
        return playerScores.computeIfAbsent(playerId, PlayerScoreRegistry::createScores);
    }

    private static @NotNull Scores createScores(UUID id) {
        return new Scores(id, new TreeSet<>());
    }
}
