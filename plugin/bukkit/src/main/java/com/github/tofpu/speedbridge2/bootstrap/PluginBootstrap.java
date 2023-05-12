package com.github.tofpu.speedbridge2.bootstrap;

import com.github.tofpu.speedbridge2.ApplicationBootstrap;
import com.github.tofpu.speedbridge2.PlayerAdapter;
import com.github.tofpu.speedbridge2.WorldAdapter;
import com.github.tofpu.speedbridge2.bootstrap.player.BukkitPlayerAdapter;
import com.github.tofpu.speedbridge2.bootstrap.world.BukkitWorldAdapter;

public class PluginBootstrap implements ApplicationBootstrap {
    @Override
    public WorldAdapter worldAdapter() {
        return new BukkitWorldAdapter();
    }

    @Override
    public PlayerAdapter playerAdapter() {
        return new BukkitPlayerAdapter();
    }
}
