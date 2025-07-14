package io.tofpu.speedbridge2.score.service;

import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.ScoreRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PlayerScoreRegistry {
    private final Map<UUID, ScoreRegistry> playerScores = new HashMap<>();
    private final Function<UUID, ScoreRegistry> registryCreator;

    public PlayerScoreRegistry(Function<UUID, ScoreRegistry> registryCreator) {
        this.registryCreator = registryCreator;
    }

    public void register(Score score) {
        UUID playerId = score.playerId();
        playerScores.computeIfAbsent(playerId, this::createScores)
                        .addScore(score);
    }

    public ScoreRegistry scores(UUID playerId) {
        return playerScores.computeIfAbsent(playerId, this::createScores);
    }

    private @NotNull ScoreRegistry createScores(UUID id) {
        return registryCreator.apply(id);
    }
}
