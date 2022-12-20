package io.tofpu.speedbridge2.plugin;

import io.tofpu.speedbridge2.SpeedBridge;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public final class SpeedBridgePlugin extends JavaPlugin {
    private final SpeedBridge speedBridge;

    public SpeedBridgePlugin() {
        this.speedBridge = new SpeedBridge(this);
    }

    public SpeedBridgePlugin(
            final JavaPluginLoader loader, final PluginDescriptionFile description, final File dataFolder,
            final File file) {
        super(loader, description, dataFolder, file);
        this.speedBridge = new SpeedBridge(this);
    }

    @Override
    public void onLoad() {
        speedBridge.load();
    }

    @Override
    public void onEnable() {
        speedBridge.enable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        speedBridge.shutdown();
    }
}
