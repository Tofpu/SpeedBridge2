package io.tofpu.speedbridge2.support.worldedit;

import io.tofpu.multiworldedit.WorldEditAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class WorldEditManager {
    public static void init(final Plugin plugin) {
        final String worldEditVersion = Bukkit.getPluginManager()
                .getPlugin("WorldEdit")
                .getDescription()
                .getVersion();
        WorldEditAPI.load(plugin, worldEditVersion);
    }
}
