package io.tofpu.speedbridge2.command.subcommand;

import io.tofpu.speedbridge2.model.island.IslandService;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;

import java.util.ArrayList;
import java.util.List;

public final class CommandCompletion {
    public static List<String> islands(final List<String> args, final CommandActor actor,
            final ExecutableCommand command) {
        final List<String> suggestions = new ArrayList<>();

        final IslandService islandService = IslandService.INSTANCE;
        for (final Integer integer : islandService.getIntegerIslands()) {
            suggestions.add(integer + "");
        }
        System.out.println("CommandCompletion#islands: suggestions - " + suggestions);

        return suggestions;
    }
}
