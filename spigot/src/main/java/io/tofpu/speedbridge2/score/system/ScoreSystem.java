package io.tofpu.speedbridge2.score.system;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.infra.db.Database;
import io.tofpu.speedbridge2.game.domain.GameFeedbackRegistry;
import io.tofpu.speedbridge2.score.infra.command.ScoreCommandHandler;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.format.rounding.ScoreRoundingHandler;
import io.tofpu.speedbridge2.score.infra.config.ScoreConfigurationLoader;
import io.tofpu.speedbridge2.score.infra.listener.PlayerScoreListener;
import io.tofpu.speedbridge2.score.persistance.ScoreDao;
import io.tofpu.speedbridge2.score.persistance.ScoreMapper;
import io.tofpu.speedbridge2.score.persistance.ScoreRepositoryImpl;
import io.tofpu.speedbridge2.score.service.ScoreService;

import java.io.File;
import java.util.Objects;

public class ScoreSystem {
    private ScoreService service;
    private ScoreFormatter formatter;

    public void load(File dataDirectory, Database database, GameFeedbackRegistry gameFeedbackRegistry, EventBus eventBus) {
        ScoreConfigurationLoader loader = new ScoreConfigurationLoader(dataDirectory);
        loader.load();

        ScoreRepositoryImpl repository = new ScoreRepositoryImpl(new ScoreDao(database), new ScoreMapper());
        this.service = new ScoreService(
                repository,
                loader.registrySettings()
        );
        this.service.loadData();

        this.formatter = new ScoreFormatter(new ScoreRoundingHandler(
                loader.formatSettings()
        ));
        eventBus.register(new PlayerScoreListener(service, gameFeedbackRegistry, formatter));
    }

    public void registerCommands(CommandHandler handler) {
        Objects.requireNonNull(service, "service is null");
        Objects.requireNonNull(formatter, "formatter is null");
        ScoreCommandHandler.register(handler, service, formatter);
    }

    public ScoreFormatter scoreFormatter() {
        return formatter;
    }

    public ScoreService scoreService() {
        return service;
    }
}
