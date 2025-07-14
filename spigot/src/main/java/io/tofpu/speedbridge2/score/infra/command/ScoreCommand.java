package io.tofpu.speedbridge2.score.infra.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
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
import revxrsal.commands.bukkit.annotation.CommandPermission;

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
}
