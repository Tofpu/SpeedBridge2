package io.tofpu.speedbridge2.command.help;

import io.tofpu.speedbridge2.command.help.collector.CommandCollector;
import io.tofpu.speedbridge2.command.help.styler.HelpMessageStyler;
import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterHolder;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import revxrsal.commands.CommandHandler;

import java.util.HashMap;
import java.util.Map;

public class HelpGenerator implements HelpMessageProvider {
    private final Map<String, Component> helpMessageMap = new HashMap<>();

    private final CommandCollector commandCollector = new CommandCollector();
    private final HelpMessageStyler messageStyler = new HelpMessageStyler();

    public void generate(Plugin plugin, CommandHandler handler) {
        Map<String, ParentCommand> parentCommands = commandCollector.collectCommands(handler);

        for (Map.Entry<String, ParentCommand> entry : parentCommands.entrySet()) {
            ParentCommand parentCommand = entry.getValue();

            final MessagePresenterHolder holder = messageStyler.create(plugin, parentCommand);
            Component helpMessageComponent = BridgeUtil.translateMiniMessage(holder.getResult());

            helpMessageMap.put(parentCommand.name(), helpMessageComponent);
            parentCommand.aliases().forEach(alias -> helpMessageMap.put(alias, helpMessageComponent));
        }
    }

    public void showHelpMessage(String parent, final CommandSender sender) {
        Component helpMessageComponent = helpMessageMap.get(parent);
        if (helpMessageComponent == null) {
            helpMessageComponent = Component.text(String.format("Help message unavailable for command %s", parent), NamedTextColor.RED);
        }
        BridgeUtil.sendMessage(sender, helpMessageComponent);
    }
}
