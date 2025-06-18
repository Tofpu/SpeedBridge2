package io.tofpu.speedbridge2.score.infra.command;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.service.ScoreService;

public class ScoreCommandHandler {
    private final CommandHandler commandHandler;

    public static ScoreCommandHandler register(CommandHandler handler, ScoreService scoreService, ScoreFormatter formatter) {
        ScoreCommandHandler scoreCommandHandler = new ScoreCommandHandler(handler);
        scoreCommandHandler.register(scoreService, formatter);
        return scoreCommandHandler;
    }

    public ScoreCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void register(ScoreService service, ScoreFormatter formatter) {
        commandHandler.addChildCommand(new ScoreCommand(service, formatter));
    }
}
