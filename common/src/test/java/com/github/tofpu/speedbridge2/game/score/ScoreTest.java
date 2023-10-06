package com.github.tofpu.speedbridge2.game.score;

import com.github.tofpu.speedbridge2.bridge.score.Score;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ScoreTest {
    @Test
    void identical_object_comparison_test() {
        UUID playerId = UUID.randomUUID();
        int islandSlot = 10;
        int seconds = 10;
        Score that = Score.inSeconds(playerId, islandSlot, seconds);
        Score other = Score.inSeconds(playerId, islandSlot, seconds);

        assertEquals(that, other);
    }

    @Test
    void different_score_comparison_test() {
        UUID playerId = UUID.randomUUID();
        int islandSlot = 10;
        Score that = Score.inSeconds(playerId, islandSlot, 10);
        Score other = Score.inSeconds(playerId, islandSlot, 20);

        assertNotEquals(that, other);
    }

    @Test
    void different_id_comparison_test() {
        int islandSlot = 10;
        int seconds = 10;
        Score that = Score.inSeconds(UUID.randomUUID(), islandSlot, seconds);
        Score other = Score.inSeconds(UUID.randomUUID(), islandSlot, seconds);

        assertNotEquals(that, other);
    }

    @Test
    void different_island_slot_comparison_test() {
        UUID playerId = UUID.randomUUID();
        int seconds = 10;
        Score that = Score.inSeconds(playerId, 10, seconds);
        Score other = Score.inSeconds(playerId, 20, seconds);

        assertNotEquals(that, other);
    }

    @Test
    void seconds_conversion_test() {
        int seconds = 10;
        Score score = Score.inSeconds(UUID.randomUUID(), -1, seconds);
        assertEquals(10, score.timerInSeconds());
    }
}
