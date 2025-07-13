package io.tofpu.speedbridge2.island.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.service.IslandService;
import java.util.Collection;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.command.CommandActor;

@Subcommand("island")
public class IslandCommand extends ChildrenCommand {
    private final IslandService islandService;

    public IslandCommand(IslandService islandService) {
        this.islandService = islandService;
    }

    private static String islandInfo(Island island) {
        return String.format(
                """
                        &7Island: &f%s
                        &8-> &7Schematic = &f%s
                        &8-> &7Group = &f%s (id %s)
                        """,
                island.slot(), island.schematic().name(), island.group().name(), island.group().id());
    }

    @Subcommand("remove")
    public void removeIsland(CommandActor actor, Island island) {
        islandService.removeIsland(island.slot());
        actor.reply("&eRemoved island &f" + island.slot());
    }

    @Subcommand("list")
    public void listIslands(CommandActor actor) {
        Collection<Island> islands = islandService.islands();
        if (islands.isEmpty()) {
            actor.reply("&cNo registered islands found.");
            return;
        }
        actor.reply("&eList of registered islands:");

        int number = 1;
        for (Island island : islands) {
            actor.reply(String.format("%d) %s", number, islandInfo(island)));
            number++;
        }
    }
}
