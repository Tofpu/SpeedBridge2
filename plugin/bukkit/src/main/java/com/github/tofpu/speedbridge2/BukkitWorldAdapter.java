package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.object.generic.World;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class BukkitWorldAdapter implements WorldAdapter {
    @Override
    public World provideWorld(String worldName) {
        return new BukkitWorld(Bukkit.createWorld(WorldCreator.name(worldName)));
    }

    @Override
    public boolean isLoadedWorld(String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }
}