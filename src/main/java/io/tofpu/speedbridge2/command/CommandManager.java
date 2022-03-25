package io.tofpu.speedbridge2.command;

import io.tofpu.speedbridge2.command.subcommand.CommandCompletion;
import io.tofpu.speedbridge2.command.subcommand.SpeedBridgeCommand;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
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
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.SenderResolver;

import java.util.UUID;

import static io.tofpu.speedbridge2.domain.common.Message.INSTANCE;

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

        commandHandler.getAutoCompleter()
                .registerParameterSuggestions(Island.class, CommandCompletion::islands);

        commandHandler.registerValueResolver(Island.class, context -> {
            final BridgePlayer player = PlayerService.INSTANCE.get(context.actor().getUniqueId());

            if (player == null) {
                throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.notLoaded));
            }

            if (player.isInSetup()) {
                throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.inASetup));
            }

            if (player.isPlaying()) {
                throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.alreadyInAIsland));
            }

            final String input = context.pop();
            final IslandService islandService = IslandService.INSTANCE;

            int slot;
            try {
                slot = Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                slot = -1;
            }

            Island island = null;
            if (slot != -1) {
                island = islandService.findIslandBy(slot);
            } else if (input != null && !input.isEmpty()) {
                island = islandService.findIslandBy(input);
            }

           if (island == null) {
               if (slot == -1) {
                   throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(INSTANCE.invalidIslandArgument));
               }
               throw new CommandErrorException(BridgeUtil.miniMessageToLegacy(String.format(INSTANCE.invalidIsland, slot + "")));
           }

           return island;
        });

        commandHandler.registerValueResolver(GameIsland.class, context -> {
            final BridgePlayer bridgePlayer = context.actor();
            return bridgePlayer.getCurrentGame();
        });

        commandHandler.register(new SpeedBridgeCommand());
    }
}
