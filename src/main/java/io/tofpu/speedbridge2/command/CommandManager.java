package io.tofpu.speedbridge2.command;

import cloud.commandframework.CommandTree;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import io.tofpu.speedbridge2.command.subcommand.SpeedBridgeCommand;
import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.EmptyBridgePlayer;
import io.tofpu.speedbridge2.domain.service.PlayerService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public final class CommandManager {
    private static BukkitCommandManager<BridgePlayer> manager;

    public static void load(final Plugin plugin) {
        final Function<CommandTree<BridgePlayer>, CommandExecutionCoordinator<BridgePlayer>> executionCoordinatorFunction = CommandExecutionCoordinator.SimpleCoordinator
                .simpleCoordinator();

        final Function<CommandSender, BridgePlayer> bridgePlayerFunction = sender -> {
            if (!(sender instanceof Player)) {
                return new EmptyBridgePlayer(sender);
            }
            return PlayerService.INSTANCE.get(((Player) sender).getUniqueId());
        };

        final Function<BridgePlayer, CommandSender> senderToBridgePlayerFunction = sender -> {
            if (sender instanceof EmptyBridgePlayer) {
                return ((EmptyBridgePlayer) sender).getSender();
            }
            return Bukkit.getPlayer(sender.getPlayerUid());
        };

        try {
            manager = new BukkitCommandManager<>(plugin, executionCoordinatorFunction, bridgePlayerFunction, senderToBridgePlayerFunction);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        final Function<ParserParameters, CommandMeta> commandMetaFunction = p -> CommandMeta
                .simple()
                // This will allow you to decorate commands with descriptions
                .with(CommandMeta.DESCRIPTION, p.get(StandardParameters.DESCRIPTION, "No description"))
                .build();

        final AnnotationParser<BridgePlayer> annotationParser = new AnnotationParser<>(
                /* Manager */ manager,
                /* Command sender type */ BridgePlayer.class,
                /* Mapper for command meta instances */ commandMetaFunction);

        annotationParser.parse(new SpeedBridgeCommand());
    }
}
