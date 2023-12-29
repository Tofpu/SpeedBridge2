package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.configuration.database.DatabaseConfiguration;
import com.github.tofpu.speedbridge2.configuration.message.ConfigurableMessageService;
import com.github.tofpu.speedbridge2.configuration.service.ConfigurationService;
import com.github.tofpu.speedbridge2.database.service.DatabaseMapper;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.service.Service;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CoreApplication {

    private static CoreApplication instance;
    private final ServiceManager serviceManager;
    private CoreBootstrap bootstrap;

    public CoreApplication() {
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
        this.serviceManager.registerAndLoad(new ConfigurableMessageService(serviceManager.get(ConfigurationService.class)));
    }

    public void enable(@NotNull CoreBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.serviceManager.loadServices();
    }

    public void disable() {
        this.serviceManager.unloadServices();
    }

    public ServiceManager serviceManager() {
        return serviceManager;
    }

    public CoreBootstrap bootstrap() {
        return bootstrap;
    }
}
