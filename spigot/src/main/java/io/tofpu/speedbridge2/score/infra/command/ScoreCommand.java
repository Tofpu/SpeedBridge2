package io.tofpu.speedbridge2.score.infra.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.domain.ScoreRegistry;
import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.service.ScoreService;
import io.tofpu.speedbridge2.util.component.EasyMessageBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;

@Subcommand("score")
public class ScoreCommand extends ChildrenCommand {
    private final ScoreService scoreService;
    private final ScoreFormatter scoreFormatter;

    public ScoreCommand(ScoreService scoreService, ScoreFormatter scoreFormatter) {
        this.scoreService = scoreService;
        this.scoreFormatter = scoreFormatter;
    }

    @Subcommand("list")
    public void list(Player sender, @Optional @CommandPermission("sb.command.score.list.target") Player target) {
        boolean self = target == null || target.equals(sender);
        UUID targetId = self ? sender.getUniqueId() : target.getUniqueId();

        ScoreRegistry scores = scoreService.scores(targetId);
        if (scores.isEmpty()) {
            sender.sendMessage(
                    text(
                            "No scores found for " + (self ? "yourself" : target.getName()) + ".",
                            NamedTextColor.RED
                    ));
            return;
        }

        sender.sendMessage(EasyMessageBuilder.create()
                .addText("{1} best scores:", NamedTextColor.GRAY, TextDecoration.BOLD)
                .addReplacement("{1}", self ? "Your" : target.getName() + "'s")
                .build());

        AtomicInteger counter = new AtomicInteger(1);
        scores.all().stream()
                .sorted()
                .forEach(score -> sender.sendMessage(EasyMessageBuilder.create()
                        .addText("{1}. {2} (island {3})", NamedTextColor.GRAY).addEmptySpace()
                        .addReplacement("{1}", String.valueOf(counter.getAndIncrement()))
                        .addReplacement("{2}", scoreFormatter.format(score), NamedTextColor.WHITE)
                        .addReplacement("{3}", score.slot() + "")
                        .build()));
    }

    @Subcommand("remove")
    public void remove(BukkitCommandActor actor, Player target, Island ignoredIsland, Score score) {
        ScoreRegistry scores = scoreService.scores(target.getUniqueId());
        if (scores.remove(score)) {
            actor.sender().sendMessage(EasyMessageBuilder.create()
                    .addText("Removed score {1} ({2}) successfully.", NamedTextColor.GREEN)
                    .addReplacement("{1}", scoreFormatter.format(score), NamedTextColor.WHITE)
                    .addReplacement("{2}", score.slot() + "", NamedTextColor.WHITE)
                    .build()
            );
        } else {
            actor.sender().sendMessage(EasyMessageBuilder.create()
                    .addText("Failed to remove score {1} ({2}).", NamedTextColor.RED)
                    .addReplacement("{1}", scoreFormatter.format(score), NamedTextColor.WHITE)
                    .addReplacement("{2}", score.slot() + "", NamedTextColor.WHITE)
                    .build()
            );
        }
    }

    @Subcommand("clear")
    public void clear(BukkitCommandActor actor, Player target, @Optional Island island) {
        ScoreRegistry registry = scoreService.scores(target.getUniqueId());
        Collection<Score> scores = island == null ? registry.all() : registry.filterByIsland(island.slot());
        // because #all or #filterbyIsland only wraps the original collection
        // so we must copy it to avoid concurrent modification exception
        scores = new ArrayList<>(scores);
        if (scores.isEmpty()) {
            String pronoun = target.equals(actor.sender()) ? "You do" : "The player {1} does";
            if (island == null) {
                actor.sender().sendMessage(EasyMessageBuilder.create()
                        .addText("{0} not have any scores to clear.", NamedTextColor.RED)
                        .addReplacement("{0}", pronoun)
                        .addReplacement("{1}", target.getName(), NamedTextColor.WHITE)
                        .build());
            } else {
                actor.sender().sendMessage(EasyMessageBuilder.create()
                        .addText("{0} not have any scores for island {2} to clear.", NamedTextColor.RED)
                        .addReplacement("{0}", pronoun)
                        .addReplacement("{1}", target.getName(), NamedTextColor.WHITE)
                        .addReplacement("{2}", island.slot() + "", NamedTextColor.WHITE)
                        .build());
            }
            return;
        }
        scores.forEach(registry::remove);
        actor.sender().sendMessage(EasyMessageBuilder.create()
                .addText("Removed {1} registry successfully.", NamedTextColor.GREEN)
                .addReplacement("{1}", scores.size() + "", NamedTextColor.WHITE)
                .build());
    }
}
