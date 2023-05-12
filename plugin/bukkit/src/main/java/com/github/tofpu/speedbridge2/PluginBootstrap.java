package com.github.tofpu.speedbridge2;

public class PluginBootstrap implements ApplicationBootstrap {
    @Override
    public WorldAdapter worldAdapter() {
        return new BukkitWorldAdapter();
    }
}
