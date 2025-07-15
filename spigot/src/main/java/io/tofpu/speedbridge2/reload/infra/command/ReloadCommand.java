package io.tofpu.speedbridge2.reload.infra.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.reload.domain.Reloadable;
import io.tofpu.speedbridge2.reload.service.ReloadService;
import io.tofpu.speedbridge2.util.component.EasyMessageBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Subcommand;

import static net.kyori.adventure.text.Component.text;

public class ReloadCommand extends ChildrenCommand {
    private final ReloadService reloadService;

    public ReloadCommand(ReloadService reloadService) {
        this.reloadService = reloadService;
    }

    @Subcommand("reload")
    public void reload(CommandSender sender, Reloadable.Key key) {
        if (key == Reloadable.Key.ALL) {
            reloadService.reloadAll();
            sender.sendMessage(text("Reloaded all components successfully.", NamedTextColor.GREEN));
        } else {
            reloadService.reload(key);
            sender.sendMessage(EasyMessageBuilder.create()
                            .addText("Reloaded {1} component successfully.", NamedTextColor.GREEN)
                            .addReplacement("{1}", key.name(), NamedTextColor.WHITE)
                    .build());
        }
    }
}
