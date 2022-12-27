package io.tofpu.speedbridge2.database.user.repository.score;

import io.tofpu.speedbridge2.model.player.object.score.Score;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserScoreService {
    CompletableFuture<Score> fetchScore(UUID key, int islandSlot);

    CompletableFuture<Boolean> isScorePresent(UUID key, int islandSlot);

    CompletableFuture<Void> insertScore(UUID key, Score score);

    CompletableFuture<Void> deleteScore(UUID key, int islandSlot);
}
