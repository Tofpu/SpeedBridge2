package com.github.tofpu.speedbridge2.bukkit.bootstrap.player;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.PlatformPlayerAdapter;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import java.util.UUID;
import org.bukkit.Bukkit;

public class BukkitPlayerAdapter implements PlatformPlayerAdapter {

    private final ConfigurationService configurationService;

    public BukkitPlayerAdapter(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public OnlinePlayer provideOnlinePlayer(UUID id) {
        requireState(isOnline(id), "Player %s is currently not online", id);
        return new BukkitOnlinePlayer(configurationService, Bukkit.getPlayer(id));
    }

    @Override
    public boolean isOnline(UUID id) {
        return Bukkit.getPlayer(id) != null;
    }
}
