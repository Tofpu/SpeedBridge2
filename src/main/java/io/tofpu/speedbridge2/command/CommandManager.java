package io.tofpu.speedbridge2.command;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.speedbridge2.command.condition.AbstractCommandConditionWrapper;
import io.tofpu.speedbridge2.command.condition.LampConditionRegistry;
import io.tofpu.speedbridge2.command.parser.AbstractLampParser;
import io.tofpu.speedbridge2.command.parser.LampParseRegistry;
import io.tofpu.speedbridge2.command.subcommand.CommandCompletion;
import io.tofpu.speedbridge2.command.subcommand.SpeedBridgeCommand;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.CommonBridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.DummyBridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.SenderBridgePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.process.SenderResolver;

import java.util.UUID;

public final class CommandManager {
    private static BukkitCommandHandler commandHandler;

    public static void load(final @NotNull Plugin plugin) {
        commandHandler = BukkitCommandHandler.create(plugin);

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
                if (actor instanceof ConsoleCommandSender) {
                    return new SenderBridgePlayer((CommandSender) actor);
                }

                final UUID uniqueId = actor.getUniqueId();
                final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uniqueId);
                if (bridgePlayer == null) {
                    return DummyBridgePlayer.of(uniqueId);
                }

                return bridgePlayer;
            }
        });

        constructTabCompleter();
        constructParsers();
        constructCommandConditions();

        commandHandler.registerResponseHandler(String.class, (response, actor, command) -> {
            if (response.isEmpty()) {
                return;
            }
            actor.reply(BridgeUtil.miniMessageToLegacy(response));
        });

        commandHandler.register(new SpeedBridgeCommand());
    }

    private static void constructTabCompleter() {
        commandHandler.getAutoCompleter()
                .registerParameterSuggestions(Island.class, CommandCompletion::islands);
    }

    private static void constructParsers() {
        final AbstractLampRegistry<?, AbstractLampParser<?>> lampParseRegistry = DynamicClass.getInstance(LampParseRegistry.class);
        if (lampParseRegistry == null) {
            return;
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
