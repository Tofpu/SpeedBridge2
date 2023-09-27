package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.plugin.BukkitPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class PluginCommandHandler {
    public void init(final BukkitPlugin plugin) {
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(plugin).registerBrigadier();

        commandHandler.registerContextResolver(OnlinePlayer.class, context -> plugin.playerAdapter().provideOnlinePlayer(context.actor().getUniqueId()));

        commandHandler.setHelpWriter((command, actor) -> String.format("/%s %s - %s", command.getPath().toRealString(), command.getUsage(), command.getDescription()));
        commandHandler.register(new PluginCommandHolder(plugin));
        commandHandler.register(new DeveloperCommandHolder(plugin));
    }
}
