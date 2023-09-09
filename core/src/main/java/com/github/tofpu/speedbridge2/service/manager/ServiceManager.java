package com.github.tofpu.speedbridge2.service.manager;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.service.LoadableService;
import com.github.tofpu.speedbridge2.service.Service;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class ServiceManager {

    private final Map<Class<? extends Service>, Service> serviceMap = new LinkedHashMap<>();

    public void register(final @NotNull Service service) {
        register(service.getClass(), service);
    }

    private void register(final Class<? extends Service> clazz, final Service service) {
        requireState(!isRegisteredService(clazz),
            "There is already a registered service with the name %s", clazz.getSimpleName());
        this.serviceMap.put(service.getClass(), service);
    }

    public void loadServices() {
        this.serviceMap.values().forEach(service -> {
            if (service instanceof LoadableService) {
                ((LoadableService) service).load();
            }
        });
    }

    public void unloadServices() {
        this.serviceMap.values().forEach(service -> {
            if (service instanceof LoadableService) {
                ((LoadableService) service).unload();
            }
        });
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T extends Service> T get(final @NotNull Class<T> clazz) {
        requireState(isRegisteredService(clazz), "There is no registered class with the name %s",
            clazz.getSimpleName());
        Service service = this.serviceMap.get(clazz);
        return (T) service;
    }

    public boolean isRegisteredService(final Class<? extends Service> service) {
        return this.serviceMap.containsKey(service);
    }
}
