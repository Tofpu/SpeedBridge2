package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.service.Service;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

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

    public void load() {
        instance = this;
        this.serviceManager.loadServices();
    }

    public void enable() {
    }
    public void disable() {
        this.serviceManager.unloadServices();
    }
}
