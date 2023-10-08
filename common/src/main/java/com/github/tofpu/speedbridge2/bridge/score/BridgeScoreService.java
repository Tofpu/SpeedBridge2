package com.github.tofpu.speedbridge2.bridge.score;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.service.LoadableService;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BridgeScoreService implements LoadableService {
    private final EventDispatcherService eventDispatcherService;
    private final ScoreRepository repository;

    private final Map<PlayerIdSlot, Scores> scoresMap = new HashMap<>();

    public BridgeScoreService(EventDispatcherService eventDispatcherService, ScoreRepository repository) {
        this.eventDispatcherService = eventDispatcherService;
        this.repository = repository;
    }

    public BridgeScoreService(ServiceManager serviceManager) {
        this(serviceManager.get(EventDispatcherService.class), new ScoreRepository(serviceManager.get(DatabaseService.class)));
    }

    public CompletableFuture<?> handleJoin(UUID playerId) {
        return repository.loadScores(playerId).whenComplete((scores, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                throw new RuntimeException("Failed to retrieve the scores for player: " + playerId, throwable);
            }

            if (scores.isEmpty()) return;

            Collections.sort(scores);
            scores.forEach(score -> addScore(score, false));
        });
    }

    private CompletableFuture<?> addScore(Score score, boolean updateDatabase) {
        PlayerIdSlot id = createId(score.getPlayerId(), score.getIslandSlot());
        Scores scores = scoresMap.get(id);
        if (scores == null) {
            System.out.println("scores not available, creating the score registry for id: " + id);
            scores = createScoreRegistry(id);
        }

        if (scores.add(score) && updateDatabase) {
            return repository.storeScore(score);
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<?> addScore(Score score) {
        return addScore(score, true);
    }

    private Scores createScoreRegistry(PlayerIdSlot id) {
        Scores value = new Scores(id);
        this.scoresMap.put(id, value);
        return value;
    }

    public CompletableFuture<?> addScore(UUID playerId, int islandSlot, double seconds) {
        return addScore(Score.inSeconds(playerId, islandSlot, seconds));
    }

    private PlayerIdSlot createId(UUID playerId, int islandSlot) {
        return new PlayerIdSlot(playerId, islandSlot);
    }

    public Scores getScores(UUID playerId, int islandSlot) {
        return this.scoresMap.get(createId(playerId, islandSlot));
    }

    public Score getBestScore(UUID playerId) {
        Optional<Score> bestScore = this.scoresMap.values().stream()
                .filter(scores -> scores.playerId().equals(playerId))
                .map(Scores::getBestScore)
                .min(Score::compareTo);
        return bestScore.orElse(null);
    }

    public Score getBestScore(UUID playerId, int islandSlot) {
        Scores scores = getScores(playerId, islandSlot);
        if (scores == null) {
            return null;
        }
        return scores.getBestScore();
    }

    @Override
    public void load() {
        eventDispatcherService.register(new ScoreListener(this));
    }

    @Override
    public void unload() {

    }

    public void clear() {
        this.scoresMap.clear();
    }
}
