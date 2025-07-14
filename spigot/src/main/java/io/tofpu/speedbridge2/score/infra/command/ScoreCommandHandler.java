package io.tofpu.speedbridge2.score.infra.command;

import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.ScoreRegistry;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.service.ScoreService;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.Collection;

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
        commandHandler.modifyBuilder(builder -> {
            builder.parameterTypes().addParameterType(Score.class, new ScoreParameterType(service, formatter));
        });
        commandHandler.addChildCommand(new ScoreCommand(service, formatter));
    }

    private record ScoreParameterType(ScoreService scoreService,
                                      ScoreFormatter scoreFormatter) implements ParameterType<BukkitCommandActor, Score> {
        @Override
        public Score parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> context) {
            Player target = context.getResolvedArgumentOrNull(Player.class);
            if (target == null) target = context.actor().requirePlayer();

            Island island = context.getResolvedArgument(Island.class);
            String timeInSeconds = input.readString();

            Collection<Score> scores = scoreService.scores(target.getUniqueId())
                    .filterByIsland(island.slot());

            for (Score score : scores) {
                String formattedDuration = scoreFormatter.format(score);
                if (formattedDuration.equals(timeInSeconds)) {
                    return score;
                }
            }
            throw new CommandErrorException("No such score: " + timeInSeconds);
        }

        @Override
        public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
            return context -> {
                Player target = context.getResolvedArgumentOrNull(Player.class);
                if (target == null) target = context.actor().requirePlayer();

                Island island = context.getResolvedArgumentOrNull(Island.class);
                ScoreRegistry scoreRegistry = scoreService.scores(target.getUniqueId());
                Collection<Score> scores;
                if (island != null) {
                    scores = scoreRegistry.filterByIsland(island.slot());
                } else {
                    scores = scoreRegistry.all();
                }
                return scores
                        .stream()
                        .map(scoreFormatter::format)
                        .toList();
            };
        }
    }
}
