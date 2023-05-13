package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.configuration.ConfigurationService;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.listener.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.service.Service;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import java.io.File;

public class SpeedBridge {

    private static SpeedBridge instance;
    private final ApplicationBootstrap bootstrap;
    private final ServiceManager serviceManager;

    public SpeedBridge(ApplicationBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.serviceManager = new ServiceManager();
    }

    public static <S extends Service> S getService(final Class<S> clazz) {
        return instance.serviceManager.get(clazz);
    }

    public void load(final File pluginDirectory) {
        instance = this;
        this.serviceManager.register(new DatabaseService());
        this.serviceManager.register(new LobbyService(getService(DatabaseService.class)));
        this.serviceManager.register(new EventDispatcherService());
        this.serviceManager.register(new ConfigurationService(pluginDirectory));
        this.serviceManager.loadServices();
    }

    public void enable() {
    }

    public void disable() {
        this.serviceManager.unloadServices();
    }
}
