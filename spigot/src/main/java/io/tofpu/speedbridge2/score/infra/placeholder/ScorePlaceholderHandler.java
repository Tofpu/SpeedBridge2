package io.tofpu.speedbridge2.score.infra.placeholder;

import io.tofpu.speedbridge2.placeholder.service.PlaceholderService;
import io.tofpu.speedbridge2.placeholder.service.PlaceholderVisitor;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.service.ScoreService;

public class ScorePlaceholderHandler implements PlaceholderVisitor {
    private final ScoreService scoreService;
    private final ScoreFormatter scoreFormatter;

    public ScorePlaceholderHandler(ScoreService scoreService, ScoreFormatter scoreFormatter) {
        this.scoreService = scoreService;
        this.scoreFormatter = scoreFormatter;
    }

    @Override
    public void visit(PlaceholderService service) {
        service.registerExpansion(new PersonalBestPlaceholder(
                scoreService, scoreFormatter
        ));
    }
}
