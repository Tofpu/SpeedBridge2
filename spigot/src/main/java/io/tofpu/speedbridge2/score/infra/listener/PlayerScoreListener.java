package io.tofpu.speedbridge2.score.infra.listener;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.game.domain.Game;
import io.tofpu.speedbridge2.game.domain.GameFeedbackRegistry;
import io.tofpu.speedbridge2.game.domain.event.GameScoreEvent;
import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.service.ScoreService;
import io.tofpu.speedbridge2.util.placeholder.Placeholder;
import org.bukkit.entity.Player;

import java.time.Instant;

public class PlayerScoreListener {
    private final ScoreService scoreService;
    private final GameFeedbackRegistry feedbackRegistry;
    private final ScoreFormatter scoreFormatter;

    public PlayerScoreListener(ScoreService scoreService, GameFeedbackRegistry feedbackRegistry, ScoreFormatter scoreFormatter) {
        this.scoreService = scoreService;
        this.feedbackRegistry = feedbackRegistry;
        this.scoreFormatter = scoreFormatter;
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

        Player player = event.getGame().gamePlayer().player();
        feedbackRegistry.get(GameFeedbackRegistry.Type.SCORE).apply(player,
                Placeholder.of("%time%", scoreFormatter.format(score))
        );
    }
}
