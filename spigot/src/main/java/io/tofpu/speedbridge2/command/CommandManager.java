package io.tofpu.speedbridge2.command;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.speedbridge2.command.condition.AbstractCommandConditionWrapper;
import io.tofpu.speedbridge2.command.condition.LampConditionRegistry;
import io.tofpu.speedbridge2.command.context.AbstractLampContext;
import io.tofpu.speedbridge2.command.context.LampContextRegistry;
import io.tofpu.speedbridge2.command.parser.AbstractLampParser;
import io.tofpu.speedbridge2.command.parser.LampParseRegistry;
import io.tofpu.speedbridge2.command.subcommand.CommandCompletion;
import io.tofpu.speedbridge2.command.subcommand.SpeedBridgeCommand;
import io.tofpu.speedbridge2.command.subcommand.debug.SpeedBridgeDebugCommand;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.player.PlayerFactory;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.CommonBridgePlayer;
import io.tofpu.speedbridge2.model.player.object.SenderBridgePlayer;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.process.SenderResolver;

import java.util.UUID;

public final class CommandManager {
    private static BukkitCommandHandler commandHandler;

    public static void load(final @NotNull Plugin plugin,
                            final @NotNull PlayerService playerService,
                            final @NotNull IslandService islandService, ArenaManager arenaManager) {
        commandHandler = BukkitCommandHandler.create(plugin);

        commandHandler.registerResponseHandler(String.class, (response, actor, command) -> {
            if (response.isEmpty()) {
                return;
            }

            final BukkitCommandActor bukkitActor = (BukkitCommandActor) actor;
            BridgeUtil.sendMessage(bukkitActor.getSender(), response);
        });

        commandHandler.registerSenderResolver(new SenderResolver() {
            @Override
            public boolean isCustomType(final Class<?> type) {
                return CommonBridgePlayer.class.isAssignableFrom(type);
            }

            @Override
            public @NotNull Object getSender(
                    @NotNull final Class<?> customSenderType,
                    @NotNull final CommandActor actor,
                    @NotNull final ExecutableCommand command) {
                final BukkitCommandActor commandActor = (BukkitCommandActor) actor;

                if (commandActor.isConsole()) {
                    return new SenderBridgePlayer(commandActor.requireConsole());
                }

                final UUID uniqueId = commandActor.requirePlayer().getUniqueId();
                final BridgePlayer bridgePlayer = playerService.getIfPresent(uniqueId);

                if (bridgePlayer != null) {
                    return bridgePlayer;
                }

                return PlayerFactory.createDummy(uniqueId);
            }
        });


        commandHandler.setHelpWriter((command, actor) -> String.format(
                "<white>- <yellow>/%s " + "%s - %s", command.getPath()
                        .toRealString(), command.getUsage(), command.getDescription()));

        constructParsers();
        constructContext();
        constructTabCompleter(islandService);
        constructCommandConditions();

        commandHandler.register(new SpeedBridgeCommand(playerService, islandService));
        commandHandler.register(new SpeedBridgeDebugCommand(arenaManager));
    }

    private static void constructTabCompleter(final @NotNull IslandService islandService) {
        BridgeUtil.debug("Constructing tab completer...");

        CommandCompletion commandCompletion = new CommandCompletion(islandService);
        commandHandler.getAutoCompleter().registerParameterSuggestions(Island.class, commandCompletion::islands);
        commandHandler.getAutoCompleter().registerParameterSuggestions(Material.class, commandCompletion::materials);
        commandHandler.getAutoCompleter().registerSuggestion("players", commandCompletion::players);
    }

    private static void constructContext() {
        final AbstractLampRegistry<?, AbstractLampContext<?>> registry =
                DynamicClass.getInstance(LampContextRegistry.class);

        if (registry == null) {
            BridgeUtil.debug("Unable to find LampContextRegistry instance... shutting down!");
            throw new RuntimeException("Unable to find LampContextRegistry instance... shutting down now!");
        }

        BridgeUtil.debug("Constructing contexts...");

        for (final AbstractLampContext<?> parser : registry.values()) {
            parser.register(commandHandler);

            BridgeUtil.debug("Registered context: " + parser.getClass()
                    .getName());
        }
    }

    private static void constructParsers() {
        final AbstractLampRegistry<?, AbstractLampParser<?>> lampParseRegistry = DynamicClass.getInstance(LampParseRegistry.class);
        if (lampParseRegistry == null) {
            BridgeUtil.debug("Unable to find LampParseRegistry instance... shutting down!");
            throw new RuntimeException("Unable to find LampParseRegistry instance... shutting down now!");
        }

        BridgeUtil.debug("Constructing parsers...");

        for (final AbstractLampParser<?> parser : lampParseRegistry.values()) {
            parser.register(commandHandler);

            BridgeUtil.debug("Registered parser: " + parser.getClass()
                    .getName());
        }
    }

    private static void constructCommandConditions() {
        final AbstractLampRegistry<?, AbstractCommandConditionWrapper> registry = DynamicClass.getInstance(LampConditionRegistry.class);
        if (registry == null) {
            return;
        }

        BridgeUtil.debug("Constructing command conditions...");

        for (final AbstractCommandConditionWrapper condition : registry.values()) {
            condition.register(commandHandler);
            BridgeUtil.debug("Registered command condition: " + condition.getClass()
                    .getName());
        }
    }
}
