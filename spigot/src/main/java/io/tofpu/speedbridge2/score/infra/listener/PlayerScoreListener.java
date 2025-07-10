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
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

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

        UUID playerId = game.gamePlayer().player().getUniqueId();
        int slot = game.island().slot();

        Score score = new Score(
                playerId,
                slot,
                timeInSeconds,
                Instant.now()
        );

        GameFeedbackRegistry.Type type = GameFeedbackRegistry.Type.SCORE;
        Collection<Placeholder> placeholders = new ArrayList<>();
        placeholders.add(Placeholder.of("%time%", scoreFormatter.format(score)));
        if (scoreService.beatenPersonalScore(playerId, slot, timeInSeconds)) {
            type = GameFeedbackRegistry.Type.BEATEN_SCORE;
            placeholders.add(Placeholder.of("%previous_time%", scoreFormatter.format(
                    scoreService.bestScore(playerId, slot)
            )));
        }

        scoreService.register(score);

        Player player = event.getGame().gamePlayer().player();
        feedbackRegistry.get(type).apply(
                player,
                placeholders.toArray(new Placeholder[0])
        );
    }
}
