package com.github.tofpu.speedbridge2.plugin;

import com.github.tofpu.speedbridge2.LogicLoader;
import com.github.tofpu.speedbridge2.SpeedBridge;
import com.github.tofpu.speedbridge2.bootstrap.PluginBootstrap;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import java.io.File;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {

    private static boolean unitTesting = false;
    private SpeedBridge speedBridge;
    private LogicLoader loader;
    private PluginBootstrap bootstrap;

    public BukkitPlugin() {
    }

    @SuppressWarnings("deprecation")
    public BukkitPlugin(PluginLoader loader, Server server,
        PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, server, description, dataFolder, file);
        unitTesting = true;
    }

    public static boolean unitTesting() {
        return unitTesting;
    }

    @Override
    public void onLoad() {
        speedBridge = new SpeedBridge();
        speedBridge.load(this.getDataFolder());

        loader = LogicLoader.load(speedBridge);
    }

    @Override
    public void onEnable() {
        bootstrap = new PluginBootstrap(speedBridge.serviceManager().get(ConfigurationService.class));
        speedBridge.enable(
                bootstrap);
        loader.enable(bootstrap);
    }

    @Override
    public void onDisable() {
        loader.disable();
        speedBridge.disable();
    }
}
