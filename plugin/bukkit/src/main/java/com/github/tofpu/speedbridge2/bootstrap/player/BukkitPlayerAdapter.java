package com.github.tofpu.speedbridge2.bootstrap.player;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.PlayerAdapter;
import com.github.tofpu.speedbridge2.player.OnlinePlayer;
import java.util.UUID;
import org.bukkit.Bukkit;

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
