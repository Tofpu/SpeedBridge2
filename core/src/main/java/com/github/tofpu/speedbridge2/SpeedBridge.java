package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.listener.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.service.Service;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import java.io.File;
import org.jetbrains.annotations.NotNull;

public class SpeedBridge {

    private static SpeedBridge instance;
    private final ServiceManager serviceManager;
    private ApplicationBootstrap bootstrap;

    public SpeedBridge() {
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

    public void enable(@NotNull ApplicationBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void disable() {
        this.serviceManager.unloadServices();
    }

    public ServiceManager serviceManager() {
        return serviceManager;
    }

    public ApplicationBootstrap bootstrap() {
        return bootstrap;
    }
}
