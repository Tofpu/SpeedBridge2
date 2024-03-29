package com.github.tofpu.speedbridge2.bukkit.plugin;

import com.github.tofpu.speedbridge2.CoreApplication;
import com.github.tofpu.speedbridge2.PlatformPlayerAdapter;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.PluginBootstrap;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.game.GameListener;
import com.github.tofpu.speedbridge2.bukkit.command.PluginCommandHandler;
import com.github.tofpu.speedbridge2.bukkit.listener.PlayerConnectionListener;
import com.github.tofpu.speedbridge2.common.CommonApplication;
import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.game.BridgeSystem;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.service.Service;
import io.tofpu.toolbar.ToolbarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class BukkitPlugin extends JavaPlugin {

    private static boolean unitTesting = false;
    private CoreApplication coreApplication;
    private CommonApplication commonApplication;
    private PluginBootstrap bootstrap;
    private ToolbarAPI toolbarAPI;

    public BukkitPlugin() {
        super();
    }

    @SuppressWarnings("deprecation")
    public BukkitPlugin(PluginLoader commonBootstrap, Server server,
                        PluginDescriptionFile description, File dataFolder, File file) {
        super(commonBootstrap, server, description, dataFolder, file);
        unitTesting = true;
    }

    public BukkitPlugin(JavaPluginLoader commonBootstrap, PluginDescriptionFile description, File dataFolder, File file) {
        super(commonBootstrap, description, dataFolder, file);
        unitTesting = true;
    }

    public static boolean unitTesting() {
        return unitTesting;
    }

    @Override
    public void onLoad() {
        coreApplication = new CoreApplication();
        coreApplication.load(this.getDataFolder());

        commonApplication = new CommonApplication(coreApplication);
        commonApplication.load();

        toolbarAPI = new ToolbarAPI(this);
    }

    @Override
    public void onEnable() {
        toolbarAPI.enable();

        try {
            bootstrap = new PluginBootstrap(this, coreApplication.serviceManager().get(ConfigurationService.class));
            coreApplication.enable(bootstrap);
            commonApplication.enable(bootstrap);

            bootstrap.init();

            if (!unitTesting) {
                new PluginCommandHandler().init(this);
            }

            Bukkit.getPluginManager().registerEvents(new GameListener(commonApplication.bridgeSystem()), this);

            PlayerConnectionListener playerConnectionListener = new PlayerConnectionListener(playerAdapter(), getService(EventDispatcherService.class));
            Bukkit.getPluginManager().registerEvents(playerConnectionListener, this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        toolbarAPI.disable();
        commonApplication.disable();
        coreApplication.disable();
    }

    public <T extends Service> T getService(final Class<T> clazz) {
        return coreApplication.serviceManager().get(clazz);
    }

    public ToolbarAPI toolbarAPI() {
        return toolbarAPI;
    }

    public PlatformPlayerAdapter playerAdapter() {
        return bootstrap.playerAdapter();
    }

    public GameSetupSystem setupSystem() {
        return commonApplication.setupSystem();
    }

    public PlatformArenaAdapter arenaAdapter() {
        return bootstrap.arenaAdapter();
    }

    public File schematicsFolder() {
        return new File(getDataFolder(), "WorldEdit/schematics");
    }

    public SchematicHandler schematicHandler() {
        return commonApplication.schematicHandler();
    }

    public BridgeSystem bridgeSystem() {
        return commonApplication.bridgeSystem();
    }
}
