package io.tofpu.speedbridge2.support.worldedit;

import io.tofpu.multiworldedit.WorldEdit;
import io.tofpu.multiworldedit.WorldEditAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class WorldEditManager {
    private static WorldEdit worldEdit;

    public static void init(final Plugin plugin) {
        final String worldEditVersion = Bukkit.getPluginManager()
                .getPlugin("WorldEdit")
                .getDescription()
                .getVersion();
        worldEdit = WorldEditAPI.load(plugin, worldEditVersion);
    }

    public static WorldEdit getWorldEdit() {
        return worldEdit;
    }
}
