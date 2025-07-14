package io.tofpu.speedbridge2.island.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.island.domain.IslandDescriptor;
import io.tofpu.speedbridge2.island.domain.UnresolvedReason;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.domain.ValidatableIsland;
import io.tofpu.speedbridge2.island.service.IslandService;

import java.util.Collection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;

import static net.kyori.adventure.text.Component.text;

@Subcommand("island")
public class IslandCommand extends ChildrenCommand {
    private final IslandService islandService;

    public IslandCommand(IslandService islandService) {
        this.islandService = islandService;
    }

    private static Component islandInfo(ValidatableIsland island) {
        String groupName = "N/A";
        if (island.isValid()) {
            groupName = (((Island) island).group().name());
        }

        Component validStatus = island.isValid() ? Component.text("")
                : text("(status: INVALID, reasons: " + reasonsToString(island.invalidReasons()), NamedTextColor.RED);
        NamedTextColor primaryColor = island.isValid() ? NamedTextColor.GRAY : NamedTextColor.RED;
        NamedTextColor secondaryColor = island.isValid() ? NamedTextColor.DARK_GRAY : NamedTextColor.DARK_RED;

        return text()
                .append(text("Island: ", primaryColor).append(text(island.slot(), NamedTextColor.WHITE).appendSpace().append(validStatus))).appendNewline()
                .append(text("-> ", secondaryColor).append(text("Schematic =", primaryColor).appendSpace().append(text(island.schematicName(), NamedTextColor.WHITE)))).appendNewline()
                .append(text("-> ", secondaryColor).append(text("Group =", primaryColor).appendSpace().append(text(groupName, NamedTextColor.WHITE).appendSpace().append(text("(id "+ island.groupId() + ")").hoverEvent(HoverEvent.showText(text("Click to copy group's id"))).clickEvent(ClickEvent.suggestCommand(island.groupId().toString()))))))
                .build();
    }

    private static String reasonsToString(UnresolvedReason[] unresolvedReasons) {
        StringBuilder builder = new StringBuilder();
        for (UnresolvedReason unresolvedReason : unresolvedReasons) {
            boolean empty = builder.isEmpty();
            if (!empty) {
                builder.append(", ");
            }
            builder.append(unresolvedReason.name());
        }
        return builder.toString();
    }

    @Subcommand("remove")
    public void removeIsland(CommandActor actor, Island island) {
        islandService.removeIsland(island.slot());
        actor.reply("&eRemoved island &f" + island.slot());
    }

    @Subcommand("list")
    public void listIslands(BukkitCommandActor actor) {
        Collection<ValidatableIsland> islands = islandService.islands(false);
        if (islands.isEmpty()) {
            actor.reply("&cNo registered islands found.");
            return;
        }
        actor.reply("&eList of registered islands:");

        int number = 1;
        for (ValidatableIsland island : islands) {
            // todo: use component here
            Component text = text(number + ")").appendSpace().append(islandInfo(island));
            actor.sender().sendMessage(text);
//            actor.reply(String.format("%d) %s", number, islandInfo(island)));
            number++;
        }
    }
}
