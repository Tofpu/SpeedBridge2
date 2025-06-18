package io.tofpu.speedbridge2.score.infra.listener;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.game.domain.Game;
import io.tofpu.speedbridge2.game.domain.event.GameScoreEvent;
import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.service.ScoreService;

import java.time.Instant;

public class PlayerScoreListener {
    private final ScoreService scoreService;

    public PlayerScoreListener(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @SubscribeEvent
    public void on(GameScoreEvent event) {
        Game game = event.getGame();
        double timeInSeconds = event.timeInSeconds();

        Score score = new Score(
                game.gamePlayer().player().getUniqueId(),
                game.island().slot(),
                timeInSeconds,
                Instant.now()
        );
        scoreService.register(score);
    }
}
