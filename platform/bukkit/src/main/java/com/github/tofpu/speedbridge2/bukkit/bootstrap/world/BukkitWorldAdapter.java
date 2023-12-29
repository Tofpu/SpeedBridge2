package com.github.tofpu.speedbridge2.bukkit.bootstrap.world;

import com.github.tofpu.speedbridge2.WorldAdapter;
import com.github.tofpu.speedbridge2.bukkit.adapter.BukkitAdapter;
import com.github.tofpu.speedbridge2.object.World;
import org.bukkit.Bukkit;

public class BukkitWorldAdapter implements WorldAdapter {

    @Override
    public World provideWorld(String worldName) {
        return BukkitAdapter.toWorld(Bukkit.getWorld(worldName));
    }

    @Override
    public boolean isLoadedWorld(String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }
}