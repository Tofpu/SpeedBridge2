package com.github.tofpu.speedbridge2.bridge.score;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.service.LoadableService;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BridgeScoreService implements LoadableService {
    private final EventDispatcherService eventDispatcherService;
    private final ScoreRepository repository;

    private final Map<UUID, PlayerIdSlot> uuidToCustomIdMapping = new HashMap<>();
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
            scores.forEach(this::addScore);
        });
    }

    private void addScore(Score score) {
        PlayerIdSlot id = getId(score.getPlayerId(), score.getIslandSlot());
        Scores scores = scoresMap.get(id);
        if (scores == null) {
            System.out.println("scores not available, creating the score registry for id: " + id);
            scores = createScoreRegistry(id);
        }
        scores.add(score);
    }

    private Scores createScoreRegistry(PlayerIdSlot id) {
        Scores value = new Scores(id);
        this.scoresMap.put(id, value);
        this.uuidToCustomIdMapping.put(id.getPlayerId(), id);
        return value;
    }

    public void addScore(UUID playerId, int islandSlot, double seconds) {
        Score score = Score.inSeconds(playerId, islandSlot, seconds);
        addScore(score);
    }

    public PlayerIdSlot getId(UUID playerId, int islandSlot) {
        return new PlayerIdSlot(playerId, islandSlot);
    }

    public PlayerIdSlot getId(UUID playerId) {
        return this.uuidToCustomIdMapping.get(playerId);
    }

    public Scores getScores(UUID playerId) {
        return this.scoresMap.get(getId(playerId));
    }

    @Override
    public void load() {
        eventDispatcherService.register(new ScoreListener(this));
    }

    @Override
    public void unload() {

    }
}
