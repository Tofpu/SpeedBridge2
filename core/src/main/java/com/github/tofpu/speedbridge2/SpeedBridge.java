package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.configuration.database.DatabaseConfiguration;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.database.service.DatabaseMapper;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.service.Service;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
        this.serviceManager.registerAndLoad(new ConfigurationService(pluginDirectory));
        this.serviceManager.register(DatabaseService.class, (serviceManager) -> {
            ConfigurationService configurationService = serviceManager.get(ConfigurationService.class);
            DatabaseConfiguration database = configurationService.config().database();
            return new DatabaseService(DatabaseMapper.map(database));
        });
        this.serviceManager.register(new EventDispatcherService());
    }

    public void enable(@NotNull ApplicationBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.serviceManager.loadServices();
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
