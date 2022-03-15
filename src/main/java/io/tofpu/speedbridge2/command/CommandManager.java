package io.tofpu.speedbridge2.command;

import cloud.commandframework.CommandTree;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import io.tofpu.speedbridge2.command.parser.GameIslandParser;
import io.tofpu.speedbridge2.command.parser.IslandParser;
import io.tofpu.speedbridge2.command.subcommand.SpeedBridgeCommand;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.CommonBridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.DummyBridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.SenderBridgePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

public final class CommandManager {
    private static BukkitCommandManager<CommonBridgePlayer> manager;

    public static void load(final @NotNull Plugin plugin) {
        final Function<CommandTree<CommonBridgePlayer>, CommandExecutionCoordinator<CommonBridgePlayer>> executionCoordinatorFunction = CommandExecutionCoordinator.SimpleCoordinator
                .simpleCoordinator();

        final Function<CommandSender, CommonBridgePlayer> bridgePlayerFunction = sender -> {
            if (sender instanceof ConsoleCommandSender) {
                return new SenderBridgePlayer(sender);
            }
            final Player player = (Player) sender;
            final UUID uniqueId = player.getUniqueId();
            final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uniqueId);

            // if the bridge player is null, return a dummy instance
            if (bridgePlayer == null) {
                return DummyBridgePlayer.of(uniqueId);
            }

            return bridgePlayer;
        };

        final Function<CommonBridgePlayer, CommandSender> senderToBridgePlayerFunction = CommonBridgePlayer::getPlayer;

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

        final AnnotationParser<CommonBridgePlayer> annotationParser = new AnnotationParser<>(
                /* Manager */ manager,
                /* Command sender type */ CommonBridgePlayer.class,
                /* Mapper for command meta instances */ commandMetaFunction);

        annotationParser.getParameterInjectorRegistry()
                .registerInjector(Island.class, (context, annotationAccessor) -> {
                    return new IslandParser<>().parse(context, annotationAccessor);
                });

        annotationParser.getParameterInjectorRegistry()
                .registerInjector(GameIsland.class, (context, annotationAccessor) -> new GameIslandParser<CommonBridgePlayer>()
                        .parse(context, annotationAccessor));

        annotationParser.parse(new SpeedBridgeCommand());
    }
}
