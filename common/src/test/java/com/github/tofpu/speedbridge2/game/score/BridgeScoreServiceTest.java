package com.github.tofpu.speedbridge2.game.score;

import com.github.tofpu.speedbridge2.bridge.score.Score;
import com.github.tofpu.speedbridge2.bridge.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.bridge.score.ScoreRepository;
import com.github.tofpu.speedbridge2.bridge.score.Scores;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BridgeScoreServiceTest {
    private final DatabaseService database = new DatabaseService();
    private final BridgeScoreService bridgeScoreService = new BridgeScoreService(new EventDispatcherService(), new ScoreRepository(database));

    @BeforeEach
    void setUp() {
        database.load();
    }

    @AfterEach
    void tearDown() {
        database.unload();
    }

    @Test
    void handle_join_test() throws ExecutionException, InterruptedException, TimeoutException {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;
        long timerInNano = TimeUnit.SECONDS.toNanos(timerInSeconds);

        database.executeAsync(session -> session.persist(Score.inNano(uuid, islandSlot, timerInNano))).get(10, TimeUnit.SECONDS);

        bridgeScoreService.handleJoin(uuid).get(10, TimeUnit.SECONDS);
        Scores scores = bridgeScoreService.getScores(uuid);
        assertNotNull(scores);

        List<Score> scoreList = scores.scoresList();
        assertEquals(1, scoreList.size());
        assertEquals(timerInNano, scoreList.get(0).getTimerInNano());
    }

    @Test
    void add_score_test() {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;
        bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds);

        Scores scores = bridgeScoreService.getScores(uuid);
        assertNotNull(scores);
        assertEquals(1, scores.size());

        Score score = scores.get(0);
        assertNotNull(scores);
        assertEquals(uuid, score.getPlayerId());
        assertEquals(islandSlot, score.getIslandSlot());
        assertEquals(timerInSeconds, score.timerInSeconds());
    }
}
