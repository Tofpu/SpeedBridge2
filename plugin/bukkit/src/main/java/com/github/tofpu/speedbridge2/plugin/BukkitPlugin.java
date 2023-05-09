package com.github.tofpu.speedbridge2.plugin;

import com.github.tofpu.speedbridge2.PluginBootstrap;
import com.github.tofpu.speedbridge2.SpeedBridge;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
    private final SpeedBridge speedBridge = new SpeedBridge(new PluginBootstrap());

    @Override
    public void onEnable() {
        speedBridge.enable();
    }

    @Override
    public void onDisable() {
        speedBridge.disable();
    }
}
