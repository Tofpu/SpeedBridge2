package io.tofpu.speedbridge2.game.infra.placeholder.type;

import io.tofpu.speedbridge2.game.GamePlayer;
import io.tofpu.speedbridge2.game.service.GameService;
import io.tofpu.speedbridge2.placeholder.domain.InputStream;
import io.tofpu.speedbridge2.placeholder.domain.PlaceholderExpansion;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CurrentTimePlaceholder extends PlaceholderExpansion {
    private final GameService gameService;
    private final ScoreFormatter scoreFormatter;

    public CurrentTimePlaceholder(GameService gameService, ScoreFormatter scoreFormatter) {
        super("current_time");
        this.gameService = Objects.requireNonNull(gameService, "GameService cannot be null");
        this.scoreFormatter = Objects.requireNonNull(scoreFormatter, "ScoreFormatter cannot be null");
    }

    @Override
    public String onRequest(Player player, InputStream inputStream) {
        return gameService.game(player.getUniqueId())
                .map(game -> {
                    GamePlayer gamePlayer = game.gamePlayer();
                    if (!gamePlayer.hasTimerStarted()) {
                        return scoreFormatter.format(0); // If the timer has not started, return 0
                    }
                    double timer = gamePlayer.currentTimerInMillis() / 1000d; // Convert milliseconds to seconds
                    return scoreFormatter.format(timer);
                }).orElse(""); // If the player is not in a game, return an empty string
    }
}
