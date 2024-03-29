package com.github.tofpu.speedbridge2.bukkit.bootstrap;

import com.github.tofpu.speedbridge2.CoreBootstrap;
import com.github.tofpu.speedbridge2.PlatformPlayerAdapter;
import com.github.tofpu.speedbridge2.PlatformWorldAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.game.BukkitGameAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.game.arena.BukkitArenaAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.player.BukkitPlayerAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.BukkitSetupAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.world.BukkitWorldAdapter;
import com.github.tofpu.speedbridge2.bukkit.plugin.BukkitPlugin;
import com.github.tofpu.speedbridge2.common.CommonBootstrap;
import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.PlatformSetupAdapter;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PluginBootstrap implements CoreBootstrap, CommonBootstrap {
    private final BukkitPlugin plugin;

    private final ConfigurationService configurationService;
    private final BukkitGameAdapter gameAdapter;
    private final BukkitSetupAdapter bukkitSetupAdapter;

    public PluginBootstrap(BukkitPlugin plugin, ConfigurationService configurationService) {
        this.plugin = plugin;
        this.configurationService = configurationService;
        this.gameAdapter = new BukkitGameAdapter();
        this.bukkitSetupAdapter = new BukkitSetupAdapter(plugin, plugin.toolbarAPI());
    }

    public void init() {
        this.bukkitSetupAdapter.register();
    }

    @Override
    public PlatformWorldAdapter worldAdapter() {
        return new BukkitWorldAdapter();
    }

    @Override
    public PlatformPlayerAdapter playerAdapter() {
        return new BukkitPlayerAdapter(configurationService);
    }

    @Override
    public PlatformArenaAdapter arenaAdapter() {
        return new BukkitArenaAdapter(plugin);
    }

    @Override
    public PlatformGameAdapter gameAdapter() {
        return gameAdapter;
    }

    @Override
    public PlatformSetupAdapter setupAdapter() {
        return bukkitSetupAdapter;
    }

    @Override
    public File schematicFolder() {
        Plugin worldEditPlugin = plugin.getServer().getPluginManager().getPlugin("WorldEdit");
        File pluginFolder = worldEditPlugin.getDataFolder();
        return new File(pluginFolder, "schematics");
    }
}
