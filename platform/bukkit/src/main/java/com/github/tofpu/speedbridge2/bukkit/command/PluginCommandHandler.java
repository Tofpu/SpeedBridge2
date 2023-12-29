package com.github.tofpu.speedbridge2.bukkit.command;

import com.github.tofpu.speedbridge2.bukkit.plugin.BukkitPlugin;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.common.schematic.exception.SchematicNotFoundException;
import com.github.tofpu.speedbridge2.common.schematic.exception.UnsupportedSchematicType;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class PluginCommandHandler {
    public void init(final BukkitPlugin plugin) {
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(plugin).registerBrigadier();

        commandHandler.registerContextResolver(OnlinePlayer.class, context -> plugin.playerAdapter().provideOnlinePlayer(context.actor().getUniqueId()));
        commandHandler.getAutoCompleter().registerSuggestion("schematics", plugin.schematicHandler().resolvedSchematics());

        registerExceptions(commandHandler);

        commandHandler.setHelpWriter((command, actor) -> String.format("/%s %s - %s", command.getPath().toRealString(), command.getUsage(), command.getDescription()));
        commandHandler.register(new PluginCommandHolder(plugin));
        commandHandler.register(new DeveloperCommandHolder(plugin));
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
