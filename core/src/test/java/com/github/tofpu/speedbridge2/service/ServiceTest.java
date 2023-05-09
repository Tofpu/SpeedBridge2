package com.github.tofpu.speedbridge2.service;

import com.github.tofpu.speedbridge2.service.manager.ServiceManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceTest {

    private ServiceManager serviceManager;

    @BeforeEach
    void setUp() {
        serviceManager = new ServiceManager();
    }

    @Test
    void register_and_get_test() {
        Assertions.assertFalse(serviceManager.isRegisteredService(DemoService.class));
        serviceManager.register(new DemoService());
        Assertions.assertTrue(serviceManager.isRegisteredService(DemoService.class));

        DemoService demoService = serviceManager.get(DemoService.class);
        demoService.increment();
        Assertions.assertEquals(1, demoService.get());

        DemoService latestDemoService = serviceManager.get(DemoService.class);
        Assertions.assertEquals(1, latestDemoService.get());
        latestDemoService.increment();
        Assertions.assertEquals(2, latestDemoService.get());
    }

    @Test
    void load_and_unload_service_test() {
        serviceManager.register(new DemoService());
        serviceManager.register(new DemoLoadableService());
        Assertions.assertFalse(serviceManager.get(DemoLoadableService.class).isLoaded());

        serviceManager.loadServices();
        Assertions.assertTrue(serviceManager.get(DemoLoadableService.class).isLoaded());

        serviceManager.unloadServices();
        Assertions.assertFalse(serviceManager.get(DemoLoadableService.class).isLoaded());
    }

    @Test
    void sanity_check() {
        Assertions.assertDoesNotThrow(() -> serviceManager.register(new DemoService()));
        Assertions.assertThrows(IllegalStateException.class, () -> serviceManager.register(new DemoService()));
    }
}
