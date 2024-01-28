package com.github.tofpu.speedbridge2.bukkit.command.subcommand;

import com.github.tofpu.speedbridge2.bukkit.command.MinimalCommandHelp;
import com.github.tofpu.speedbridge2.bukkit.util.MessageBuilder;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.orphan.OrphanCommand;

public abstract class CoreCommand implements OrphanCommand {
    @NotNull
    public abstract String name();

    @Subcommand("help")
    @Description("View the available commands")
    public void help(final OnlinePlayer player, final MinimalCommandHelp<String> helpEntries) {
        MessageBuilder messageBuilder = new MessageBuilder()
                .appendNewLine()
                .append("&8&l|>&r &e&lSpeedBridge3 &fv2.0-dev &8| &e&l%s Help".replace("%s", capitalizedName())).appendNewLine()
                .appendNewLine()
                .append("&8&l|- &e%s commands:".replace("%s", capitalizedName())).appendNewLine();

        for (String entry : helpEntries) {
            messageBuilder.append(entry).appendNewLine();
        }

        player.sendMessage(messageBuilder.toString());
    }

    @NotNull
    private String capitalizedName() {
        return WordUtils.capitalize(name());
    }
}
