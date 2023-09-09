package com.github.tofpu.speedbridge2.bootstrap;

import com.github.tofpu.speedbridge2.*;
import com.github.tofpu.speedbridge2.bootstrap.game.arena.IslandArenaAdapter;
import com.github.tofpu.speedbridge2.bootstrap.player.BukkitPlayerAdapter;
import com.github.tofpu.speedbridge2.bootstrap.world.BukkitWorldAdapter;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;

public class PluginBootstrap implements ApplicationBootstrap, LogicBootStrap {

    private final ConfigurationService configurationService;

    public PluginBootstrap(ConfigurationService configurationService) {
        this.configurationService = configurationService;
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
        return new IslandArenaAdapter();
    }
}
