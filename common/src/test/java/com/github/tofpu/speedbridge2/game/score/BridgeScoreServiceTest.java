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

import static org.junit.jupiter.api.Assertions.*;

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

        database.executeAsync(session -> session.persist(Score.inNanoSeconds(uuid, islandSlot, timerInNano))).get(10, TimeUnit.SECONDS);

        bridgeScoreService.handleJoin(uuid).get(10, TimeUnit.SECONDS);
        Scores scores = bridgeScoreService.getScores(uuid, 1);
        assertNotNull(scores);

        List<Score> scoreList = scores.scoresList();
        assertEquals(1, scoreList.size());
        assertEquals(timerInNano, scoreList.get(0).getNanoseconds());
    }

    @Test
    void add_score_test() {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;
        bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds);

        Scores scores = bridgeScoreService.getScores(uuid, 1);
        assertNotNull(scores);
        assertEquals(1, scores.size());

        Score score = scores.get(0);
        assertNotNull(scores);
        assertEquals(uuid, score.getPlayerId());
        assertEquals(islandSlot, score.getIslandSlot());
        assertEquals(timerInSeconds, score.seconds());
    }

    @Test
    void add_score_and_fetch_test() throws ExecutionException, InterruptedException, TimeoutException {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;
        bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds).get(10, TimeUnit.SECONDS);

        bridgeScoreService.clear();
        bridgeScoreService.handleJoin(uuid).get(10, TimeUnit.SECONDS);

        Scores scores = bridgeScoreService.getScores(uuid, islandSlot);
        assertNotNull(scores);
        Score score = scores.get(0);
        assertNotNull(score);
        assertEquals(1, score.getIslandSlot());
        assertEquals(10, score.seconds());
    }

    @Test
    void add_identical_scores_and_fetch_test() throws ExecutionException, InterruptedException, TimeoutException {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;
        bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds).get(10, TimeUnit.SECONDS);
        bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds).get(10, TimeUnit.SECONDS);

        bridgeScoreService.clear();
        bridgeScoreService.handleJoin(uuid).get(10, TimeUnit.SECONDS);

        Scores scores = bridgeScoreService.getScores(uuid, islandSlot);
        assertEquals(2, scores.size());
    }

    @Test
    void add_different_scores_on_same_island_and_fetch_test() throws ExecutionException, InterruptedException, TimeoutException {
        UUID uuid = UUID.randomUUID();
        Score firstScore = Score.inSeconds(uuid, 1, 10);
        Score secondScore = Score.inSeconds(uuid, 1, 20);
        bridgeScoreService.addScore(firstScore).get(10, TimeUnit.SECONDS);
        bridgeScoreService.addScore(secondScore).get(10, TimeUnit.SECONDS);

        bridgeScoreService.clear();
        bridgeScoreService.handleJoin(uuid).get(10, TimeUnit.SECONDS);

        Scores scores = bridgeScoreService.getScores(uuid, 1);
        assertEquals(2, scores.size());
        assertEquals(firstScore, scores.get(0));
        assertEquals(secondScore, scores.get(1));
    }

    @Test
    void maximum_score_entry_test() {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;

        // adds scores of: 10, 20, 30, 40, and 50
        for (int i = 1; i <= Scores.MAXIMUM_SIZE; i++) {
            bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds * i);
        }

        Scores scores = bridgeScoreService.getScores(uuid, islandSlot);
        assertEquals(Scores.MAXIMUM_SIZE, scores.size());
    }

    @Test
    void exceed_maximum_size_of_score_registry_test() {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;

        // adds scores of: 10, 20, 30, 40, 50, and 60
        for (int i = 1; i <= Scores.MAXIMUM_SIZE + 1; i++) {
            System.out.println("i=" + i);
            bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds * i);
        }

        Scores scores = bridgeScoreService.getScores(uuid, islandSlot);
        assertEquals(Scores.MAXIMUM_SIZE, scores.size());
    }

    @Test
    void score_override_in_case_of_reaching_maximum_score_test() {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;

        // adds scores of: 10, 20, 30, 40, and 50
        for (int i = 1; i <= 5; i++) {
            bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds * i);
        }
        // to overtake the last score
        int fifthNewScore = 49;
        bridgeScoreService.addScore(uuid, islandSlot, fifthNewScore);

        Scores scores = bridgeScoreService.getScores(uuid, islandSlot);
        Score fifthScore = scores.get(4);
        assertEquals(fifthNewScore, fifthScore.seconds());
    }

    @Test
    void get_best_score_with_multiple_entry_in_same_island() {
        UUID uuid = UUID.randomUUID();
        int islandSlot = 1;
        int timerInSeconds = 10;
        bridgeScoreService.addScore(uuid, islandSlot, 20);
        bridgeScoreService.addScore(uuid, islandSlot, timerInSeconds);

        Score bestScore = bridgeScoreService.getBestScore(uuid);
        assertNotNull(bestScore);
        assertEquals(islandSlot, bestScore.getIslandSlot());
        assertEquals(timerInSeconds, bestScore.seconds());
    }

    @Test
    void get_best_score_with_multiple_entry_in_different_island() {
        UUID uuid = UUID.randomUUID();
        bridgeScoreService.addScore(uuid, 10, 10);
        bridgeScoreService.addScore(uuid, 20, 20);

        Score bestScore = bridgeScoreService.getBestScore(uuid);
        assertEquals(10, bestScore.getIslandSlot());
        assertEquals(10, bestScore.seconds());
    }

    @Test
    void get_score_with_no_entries() {
        Scores scores = bridgeScoreService.getScores(UUID.randomUUID(), 10);
        assertNull(scores);
    }

    @Test
    void get_best_score_with_no_entries() {
        Score bestScore = bridgeScoreService.getBestScore(UUID.randomUUID());
        assertNull(bestScore);
    }

    @Test
    void add_wrong_score_to_score_registry() {
        UUID playerId = UUID.randomUUID();
        int islandSlot = 1;
        bridgeScoreService.addScore(playerId, islandSlot, 10);
        Scores scores = bridgeScoreService.getScores(playerId, islandSlot);

        int differentIslandSlot = 2;
        assertThrows(IllegalStateException.class, () -> {
            scores.add(Score.inSeconds(playerId, differentIslandSlot, 9));
        });
    }
}
