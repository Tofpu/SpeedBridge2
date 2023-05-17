package com.github.tofpu.speedbridge2.plugin;

import com.github.tofpu.speedbridge2.SpeedBridge;
import com.github.tofpu.speedbridge2.bootstrap.PluginBootstrap;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
    private SpeedBridge speedBridge;

    @Override
    public void onLoad() {
        speedBridge = new SpeedBridge();
        speedBridge.load(this.getDataFolder());
    }

    @Override
    public void onEnable() {
        speedBridge.enable(new PluginBootstrap(speedBridge.serviceManager().get(ConfigurationService.class)));
    }

    @Override
    public void onDisable() {
        speedBridge.disable();
    }
}
