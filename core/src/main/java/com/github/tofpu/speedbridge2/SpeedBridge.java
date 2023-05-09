package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import com.github.tofpu.speedbridge2.user.UserService;

public class SpeedBridge {
    private final ApplicationBootstrap bootstrap;
    private final ServiceManager serviceManager;

    public SpeedBridge(ApplicationBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.serviceManager = new ServiceManager();
    }

    public void load() {
        this.serviceManager.register(new UserService());
        this.serviceManager.loadServices();
    }

    public void enable() {
    }
    public void disable() {
        this.serviceManager.unloadServices();
    }
}
