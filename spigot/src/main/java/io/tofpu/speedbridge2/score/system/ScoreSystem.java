package io.tofpu.speedbridge2.score.system;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.database.infra.db.Database;
import io.tofpu.speedbridge2.game.domain.GameFeedbackRegistry;
import io.tofpu.speedbridge2.placeholder.service.PlaceholderService;
import io.tofpu.speedbridge2.score.domain.ScoreRegistry;
import io.tofpu.speedbridge2.score.infra.command.ScoreCommandHandler;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.format.rounding.ScoreRoundingHandler;
import io.tofpu.speedbridge2.score.infra.config.ScoreConfigurationLoader;
import io.tofpu.speedbridge2.score.infra.listener.PlayerScoreListener;
import io.tofpu.speedbridge2.score.infra.listener.ScoreRegistrationChangeListener;
import io.tofpu.speedbridge2.score.infra.placeholder.ScorePlaceholderHandler;
import io.tofpu.speedbridge2.score.persistance.ScoreDao;
import io.tofpu.speedbridge2.score.persistance.ScoreMapper;
import io.tofpu.speedbridge2.score.persistance.ScoreRepositoryImpl;
import io.tofpu.speedbridge2.score.service.PlayerScoreRegistry;
import io.tofpu.speedbridge2.score.service.ScoreRegistryImpl;
import io.tofpu.speedbridge2.score.service.ScoreService;

import java.io.File;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;

public class ScoreSystem {
    private ScoreService service;
    private ScoreFormatter formatter;

    public void load(File dataDirectory, Database database, GameFeedbackRegistry gameFeedbackRegistry, EventBus eventBus) {
        ScoreConfigurationLoader loader = new ScoreConfigurationLoader(dataDirectory);
        loader.load();

        ScoreRepositoryImpl repository = new ScoreRepositoryImpl(new ScoreDao(database), new ScoreMapper());
        Function<UUID, ScoreRegistry> scoreRegistryCreator = playerId -> new ScoreRegistryImpl(eventBus, playerId, new TreeSet<>());
        this.service = new ScoreService(
                repository,
                loader.registrySettings(),
                new PlayerScoreRegistry(scoreRegistryCreator)
        );
        this.service.loadData();

        this.formatter = new ScoreFormatter(new ScoreRoundingHandler(
                loader.formatSettings()
        ));

        eventBus.register(new PlayerScoreListener(service, gameFeedbackRegistry, formatter));
        eventBus.register(new ScoreRegistrationChangeListener(repository));
    }

    public void registerCommands(CommandHandler handler) {
        Objects.requireNonNull(service, "service is null");
        Objects.requireNonNull(formatter, "formatter is null");
        ScoreCommandHandler.register(handler, service, formatter);
    }

    public void registerPlaceholders(PlaceholderService placeholderService) {
        Objects.requireNonNull(service, "service is null");
        Objects.requireNonNull(formatter, "formatter is null");
        new ScorePlaceholderHandler(service, formatter).visit(placeholderService);
    }

    public ScoreFormatter scoreFormatter() {
        return formatter;
    }

    public ScoreService scoreService() {
        return service;
    }
}
