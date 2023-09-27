package com.github.tofpu.speedbridge2.bootstrap;

import com.github.tofpu.speedbridge2.*;
import com.github.tofpu.speedbridge2.bootstrap.game.BukkitGameAdapter;
import com.github.tofpu.speedbridge2.bootstrap.game.arena.BukkitArenaAdapter;
import com.github.tofpu.speedbridge2.bootstrap.player.BukkitPlayerAdapter;
import com.github.tofpu.speedbridge2.bootstrap.world.BukkitWorldAdapter;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PluginBootstrap implements ApplicationBootstrap, LogicBootStrap {
    private final Plugin plugin;

    private final ConfigurationService configurationService;
    private final BukkitGameAdapter gameAdapter;

    public PluginBootstrap(Plugin plugin, ConfigurationService configurationService) {
        this.plugin = plugin;
        this.configurationService = configurationService;
        this.gameAdapter = new BukkitGameAdapter();
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
    public GameAdapter gameAdapter() {
        return gameAdapter;
    }

    @Override
    public File schematicFolder() {
        Plugin worldEditPlugin = plugin.getServer().getPluginManager().getPlugin("WorldEdit");
        File pluginFolder = worldEditPlugin.getDataFolder();
        return new File(pluginFolder, "schematics");
    }
}
