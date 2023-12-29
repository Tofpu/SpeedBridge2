package com.github.tofpu.speedbridge2.bukkit.bootstrap.player;

import com.github.tofpu.speedbridge2.bukkit.helper.CoreConversionHelper;
import com.github.tofpu.speedbridge2.bukkit.helper.BukkitConversionHelper;
import com.github.tofpu.speedbridge2.configuration.service.PluginConfigTypes;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import java.util.UUID;
import org.bukkit.entity.Player;

public class BukkitOnlinePlayer implements OnlinePlayer {

    private final ConfigurationService configurationService;

    private final Player player;

    public BukkitOnlinePlayer(ConfigurationService configurationService, Player player) {
        this.configurationService = configurationService;
        this.player = player;
    }

    @Override
    public UUID id() {
        return player.getUniqueId();
    }

    @Override
    public String name() {
        return player.getName();
    }

    @Override
    public void sendMessage(String content) {
        player.sendMessage(content);
    }

    @Override
    public void sendMessage(ConfigurableMessage message) {
        sendMessage(configurationService.on(PluginConfigTypes.MESSAGE)
            .getString(message.key(), message.defaultMessage()));
    }

    @Override
    public void sendMessage(ConfigurableMessage message, Object... replace) {
        sendMessage(message.defaultMessage(replace));
    }

    @Override
    public void teleport(Position position) {
        player.teleport(BukkitConversionHelper.toLocation(position));
    }

    @Override
    public void teleport(Location location) {
        player.teleport(BukkitConversionHelper.toLocation(location));
    }

    @Override
    public boolean hasPermission(String node) {
        return player.hasPermission(node);
    }

    @Override
    public boolean isOperator() {
        return player.isOp();
    }

    @Override
    public World getWorld() {
        return CoreConversionHelper.toWorld(player.getWorld());
    }

    @Override
    public Position getPosition() {
        return CoreConversionHelper.toPosition(player.getLocation());
    }
}
