package com.github.tofpu.speedbridge2.service.manager;

import com.github.tofpu.speedbridge2.service.LoadableService;
import com.github.tofpu.speedbridge2.service.Service;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class ServiceManager {

    private final Map<Class<? extends Service>, Service> serviceMap = new LinkedHashMap<>();
    private final List<Class<?>> loadIgnoreList = new ArrayList<>();

    public void register(final @NotNull Service service) {
        register(service.getClass(), service);
    }

    public <T extends Service> void register(final Class<T> serviceType, final @NotNull Function<ServiceManager, T> serviceFunction) {
        Service service = serviceFunction.apply(this);
        requireState(serviceType.isAssignableFrom(service.getClass()), "Excepted service %s, and not %s service", serviceType.getSimpleName(), service.getClass().getSimpleName());
        register(service);
    }

    public void registerAndLoad(final @NotNull LoadableService service) {
        Class<? extends @NotNull LoadableService> serviceType = service.getClass();
        register(serviceType, service);
        loadIgnoreList.add(serviceType);
        service.load();
    }

    private void register(final Class<? extends Service> clazz, final Service service) {
        requireServiceToBeNotRegisteredBefore(clazz);
        this.serviceMap.put(clazz, service);
    }

    private void requireServiceToBeNotRegisteredBefore(Class<? extends Service> clazz) {
        requireState(!isRegisteredService(clazz),
                "There is already a registered service with the name %s", clazz.getSimpleName());
    }

    public void loadServices() {
        this.serviceMap.values().forEach(service -> {
            if (loadIgnoreList.contains(service.getClass())) return;
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
