package io.tofpu.speedbridge2.game.infra.placeholder;

import io.tofpu.speedbridge2.game.infra.placeholder.type.GameBlockCountPlaceholder;
import io.tofpu.speedbridge2.game.infra.placeholder.type.CurrentTimePlaceholder;
import io.tofpu.speedbridge2.game.service.GameService;
import io.tofpu.speedbridge2.placeholder.service.PlaceholderService;
import io.tofpu.speedbridge2.placeholder.service.PlaceholderVisitor;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;

public class GamePlaceholderHandler implements PlaceholderVisitor {

    private final GameService gameService;
    private final ScoreFormatter scoreFormatter;

    public GamePlaceholderHandler(GameService gameService, ScoreFormatter scoreFormatter) {
        this.gameService = gameService;
        this.scoreFormatter = scoreFormatter;
    }

    @Override
    public void visit(PlaceholderService service) {
        service.registerExpansion(
                new CurrentTimePlaceholder(gameService, scoreFormatter),
                new GameBlockCountPlaceholder(gameService)
        );
    }
}
