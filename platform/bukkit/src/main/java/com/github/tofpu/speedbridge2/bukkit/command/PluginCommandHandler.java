package com.github.tofpu.speedbridge2.bukkit.command;

import com.github.tofpu.speedbridge2.bukkit.BukkitMessages;
import com.github.tofpu.speedbridge2.bukkit.command.annotation.RequireLobbyToBeAvailable;
import com.github.tofpu.speedbridge2.bukkit.command.subcommand.CoreCommandExtension;
import com.github.tofpu.speedbridge2.bukkit.plugin.BukkitPlugin;
import com.github.tofpu.speedbridge2.bukkit.util.ChatUtil;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.schematic.exception.SchematicNotFoundException;
import com.github.tofpu.speedbridge2.common.schematic.exception.UnsupportedSchematicType;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.core.CommandPath;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.orphan.Orphans;

import java.util.Arrays;

public class PluginCommandHandler {
    public void init(final BukkitPlugin plugin) {
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(plugin).registerBrigadier();

        commandHandler.registerContextResolver(OnlinePlayer.class, context -> plugin.playerAdapter().provideOnlinePlayer(context.actor().getUniqueId()));
        commandHandler.getAutoCompleter().registerSuggestion("schematics", plugin.schematicHandler().resolvedSchematics());

        registerExceptions(commandHandler);

        commandHandler.setHelpWriter((command, actor) -> String.format("/%s %s - %s", command.getPath().toRealString(), command.getUsage(), command.getDescription()));
        commandHandler.registerContextResolver((Class) MinimalCommandHelp.class, new MinimalCommandHelpResolver(commandHandler));

        commandHandler.register(new PluginCommandHolder(plugin));
        commandHandler.register(new DeveloperCommandHolder(plugin));

        commandHandler.registerValueResolver(Island.class, context -> {
            int islandSlot = context.popInt();
            Island island = plugin.getService(IslandService.class).get(islandSlot);
            if (island == null) {
                throw new CommandErrorException(BukkitMessages.NONEXISTENT_ISLAND.defaultMessage(islandSlot));
            }
            return island;
        });

        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!command.hasAnnotation(RequireLobbyToBeAvailable.class)) return;
            boolean lobbyAvailable = plugin.getService(LobbyService.class).isLobbyAvailable();
            if (!lobbyAvailable) {
                throw new CommandErrorException(BukkitMessages.LOBBY_NOT_AVAILABLE.defaultMessage());
            }
        });

        commandHandler.setHelpWriter((command, actor) -> {
            String description = command.getDescription();
            if (description == null) {
                description = "No description provided!";
            }
            CommandPath commandPath = command.getPath();
            String last = commandPath.getLast();
            if (commandPath.getFirst().equals(last)) {
                return ChatUtil.colorize(String.format("&8|-> /&e%s &f%s &8- &7%s", commandPath.toRealString(), command.getUsage(), description));
            }
            String pathExcludingLast = commandPath.toRealString().replace(" " + last, "");
            return ChatUtil.colorize(String.format("&8|-> /&e%s &f%s &e%s &r&8- &7%s", pathExcludingLast, last, command.getUsage(), description).trim());
        });

        Arrays.stream(new CoreCommandExtension().commands(plugin.bridgeSystem(), plugin.setupSystem()))
                .forEach(coreCommand -> {
                    String mainPath = "speedbridge %s".replace("%s", coreCommand.name()).trim();
                    String aliasPath = "sb %s".replace("%s", coreCommand.name()).trim();
                    commandHandler.register(Orphans.path(mainPath, aliasPath).handler(coreCommand));
                });
    }

    private void registerExceptions(BukkitCommandHandler commandHandler) {
        commandHandler.registerExceptionHandler(SchematicNotFoundException.class, (commandActor, e) -> {
            commandActor.error("Unable to find a schematic with the name: " + e.file().getName());
        });
        commandHandler.registerExceptionHandler(UnsupportedSchematicType.class, (commandActor, e) -> {
            commandActor.error("Unable to recognize this schematic: " + e.file());
        });
    }
}
