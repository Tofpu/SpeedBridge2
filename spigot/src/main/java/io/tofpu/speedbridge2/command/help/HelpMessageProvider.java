package io.tofpu.speedbridge2.command.help;

import org.bukkit.command.CommandSender;

public interface HelpMessageProvider {
    void showHelpMessage(String parent, final CommandSender sender);
}
