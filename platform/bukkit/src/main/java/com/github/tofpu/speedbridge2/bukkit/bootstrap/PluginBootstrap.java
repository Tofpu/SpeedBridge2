package com.github.tofpu.speedbridge2.bukkit.bootstrap;

import com.github.tofpu.speedbridge2.*;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.game.BukkitPlatformGameAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.game.arena.BukkitArenaAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.player.BukkitPlayerAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.world.BukkitWorldAdapter;
import com.github.tofpu.speedbridge2.common.ArenaAdapter;
import com.github.tofpu.speedbridge2.common.CommonBootstrap;
import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PluginBootstrap implements CoreBootstrap, CommonBootstrap {
    private final Plugin plugin;

    private final ConfigurationService configurationService;
    private final BukkitPlatformGameAdapter gameAdapter;

    public PluginBootstrap(Plugin plugin, ConfigurationService configurationService) {
        this.plugin = plugin;
        this.configurationService = configurationService;
        this.gameAdapter = new BukkitPlatformGameAdapter();
    }

    @Override
    public WorldAdapter worldAdapter() {
        return new BukkitWorldAdapter();
    }

    @Override
    public PlayerAdapter playerAdapter() {
        return new BukkitPlayerAdapter(configurationService);
    }

    @Override
    public ArenaAdapter arenaAdapter() {
        return new BukkitArenaAdapter(plugin);
    }

    @Override
    public PlatformGameAdapter gameAdapter() {
        return gameAdapter;
    }

    @Override
    public File schematicFolder() {
        Plugin worldEditPlugin = plugin.getServer().getPluginManager().getPlugin("WorldEdit");
        File pluginFolder = worldEditPlugin.getDataFolder();
        return new File(pluginFolder, "schematics");
    }
}
