package io.tofpu.speedbridge2.score.service;

import io.tofpu.speedbridge2.score.domain.RegistrySettings;
import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.ScoreRepository;
import io.tofpu.speedbridge2.score.domain.Scores;

import java.util.Collection;
import java.util.UUID;

public class ScoreService {
    private final ScoreRepository repository;
    private final PlayerScoreRegistry playerScoreRegistry;
    private final RegistrySettings registrySettings;

    public ScoreService(ScoreRepository repository, RegistrySettings registrySettings) {
        this.repository = repository;
        this.registrySettings = registrySettings;
        this.playerScoreRegistry = new PlayerScoreRegistry();
    }

    public void loadData() {
        repository.findAll().forEach(playerScoreRegistry::register);
    }

    public void register(Score score) {
        Scores playerScores = playerScoreRegistry.scores(score.playerId());
        Collection<Score> islandScores = playerScores.filterByIsland(score.slot());
        int size = islandScores.size();

        repository.save(score);
        playerScoreRegistry.register(score);

        // reaches the score size limit per island per player
        if (registrySettings.entriesLimit() <= size) {
            Score removed = playerScores.removeLastScore();
            repository.delete(removed);
        }
    }

    public Scores scores(UUID playerId) {
        return playerScoreRegistry.scores(playerId);
    }
}
