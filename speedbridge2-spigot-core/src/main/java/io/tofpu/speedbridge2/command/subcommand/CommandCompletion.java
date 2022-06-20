package io.tofpu.speedbridge2.command.subcommand;

import io.tofpu.speedbridge2.model.island.IslandService;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;

import java.util.ArrayList;
import java.util.List;

public final class CommandCompletion {
    private final IslandService islandService;

    public CommandCompletion(final IslandService islandService) {
        this.islandService = islandService;
    }

    public @NotNull List<String> islands(final List<String> args, final CommandActor actor,
            final ExecutableCommand command) {
        final List<String> suggestions = new ArrayList<>();

        for (final Integer integer : islandService.getIntegerIslands()) {
            suggestions.add(integer + "");
        }
        System.out.println("CommandCompletion#islands: suggestions - " + suggestions);

        return suggestions;
    }
}
