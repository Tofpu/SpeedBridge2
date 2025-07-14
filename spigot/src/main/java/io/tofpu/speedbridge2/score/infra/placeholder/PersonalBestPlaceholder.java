package io.tofpu.speedbridge2.score.infra.placeholder;

import io.tofpu.speedbridge2.placeholder.domain.InputStream;
import io.tofpu.speedbridge2.placeholder.domain.PlaceholderExpansion;
import io.tofpu.speedbridge2.score.domain.ScoreRegistry;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.service.ScoreService;
import org.bukkit.entity.Player;

public class PersonalBestPlaceholder extends PlaceholderExpansion {
    private final ScoreService scoreService;
    private final ScoreFormatter scoreFormatter;

    public PersonalBestPlaceholder(ScoreService scoreService, ScoreFormatter scoreFormatter) {
        super("personal_best");
        this.scoreService = scoreService;
        this.scoreFormatter = scoreFormatter;
    }

    @Override
    public String onRequest(Player player, InputStream inputStream) {
        ScoreRegistry scores = scoreService.scores(player.getUniqueId());
        return scores.bestScore()
                .map(scoreFormatter::format)
                .orElse("No personal best");
    }
}
