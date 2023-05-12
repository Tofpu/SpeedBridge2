package com.github.tofpu.speedbridge2.bootstrap.player;

import com.github.tofpu.speedbridge2.PlayerAdapter;
import com.github.tofpu.speedbridge2.player.OnlinePlayer;
import org.bukkit.Bukkit;

import java.util.UUID;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class BukkitPlayerAdapter implements PlayerAdapter {
    @Override
    public OnlinePlayer provideOnlinePlayer(UUID id) {
        requireState(isOnline(id), "Player %s is currently not online", id);
        return new BukkitOnlinePlayer(Bukkit.getPlayer(id));
    }

    @Override
    public boolean isOnline(UUID id) {
        return Bukkit.getPlayer(id) != null;
    }
}
