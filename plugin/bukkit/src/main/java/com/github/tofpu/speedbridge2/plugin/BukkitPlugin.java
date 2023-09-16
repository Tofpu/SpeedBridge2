package com.github.tofpu.speedbridge2.plugin;

import com.github.tofpu.speedbridge2.LogicLoader;
import com.github.tofpu.speedbridge2.PlayerAdapter;
import com.github.tofpu.speedbridge2.SpeedBridge;
import com.github.tofpu.speedbridge2.bootstrap.PluginBootstrap;
import com.github.tofpu.speedbridge2.command.PluginCommandHandler;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import java.io.File;

import com.github.tofpu.speedbridge2.game.island.IslandGameHandler;
import com.github.tofpu.speedbridge2.island.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.island.setup.IslandSetupListener;
import com.github.tofpu.speedbridge2.service.Service;
import org.bukkit.Bukkit;
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
        bootstrap = new PluginBootstrap(this, speedBridge.serviceManager().get(ConfigurationService.class));
        speedBridge.enable(
                bootstrap);
        loader.enable(bootstrap);

        new PluginCommandHandler().init(this);
        IslandSetupListener islandSetupListener = new IslandSetupListener(loader.setupController());
        Bukkit.getPluginManager().registerEvents(islandSetupListener, this);
    }

    @Override
    public void onDisable() {
        loader.disable();
        speedBridge.disable();
    }

    public <T extends Service> T getService(final Class<T> clazz) {
        return speedBridge.serviceManager().get(clazz);
    }

    public PlayerAdapter playerAdapter() {
        return bootstrap.playerAdapter();
    }

    public IslandSetupController setupController() {
        return loader.setupController();
    }

    public IslandGameHandler gameHandler() {
        return loader.gameHandler();
    }

    public File schematicsFolder() {
        return new File(getDataFolder(), "WorldEdit/schematics");
    }
}
