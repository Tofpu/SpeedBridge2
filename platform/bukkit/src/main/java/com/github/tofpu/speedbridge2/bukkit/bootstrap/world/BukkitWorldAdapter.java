package com.github.tofpu.speedbridge2.bukkit.bootstrap.world;

import com.github.tofpu.speedbridge2.PlatformWorldAdapter;
import com.github.tofpu.speedbridge2.bukkit.helper.CoreConversionHelper;
import com.github.tofpu.speedbridge2.object.World;
import org.bukkit.Bukkit;

public class BukkitWorldAdapter implements PlatformWorldAdapter {

    @Override
    public World provideWorld(String worldName) {
        return CoreConversionHelper.toWorld(Bukkit.getWorld(worldName));
    }

    @Override
    public boolean isLoadedWorld(String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }
}