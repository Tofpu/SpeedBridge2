package com.github.tofpu.speedbridge2.service.manager;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.service.LoadableService;
import com.github.tofpu.speedbridge2.service.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

public class ServiceManager {

    private final Map<Class<? extends Service>, Service> serviceMap = new LinkedHashMap<>();
//    private final Map<Class<? extends Service>, Function<ServiceManager, Service>> lazyServiceMap = new HashMap<>();

    public void register(final @NotNull Service service) {
        register(service.getClass(), service);
    }

    public void registerLazy(final Class<? extends Service> serviceType, final @NotNull Function<ServiceManager, Service> serviceFunction) {
        requireServiceToBeNotRegisteredBefore(serviceType);
//        lazyServiceMap.put(serviceType, serviceFunction);
        register(serviceType, LazyService.wrap(serviceFunction));
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
            if (service instanceof LazyService) {
                service = ((LazyService) service).load(this);
            }
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
        if (service instanceof LazyService) {
            service = ((LazyService) service).service();
        }
        return (T) service;
    }

    public boolean isRegisteredService(final Class<? extends Service> service) {
        return this.serviceMap.containsKey(service);
    }

    private static class LazyService implements Service {
        private final Function<ServiceManager, Service> serviceFunction;
        private Service service;

        public LazyService(Function<ServiceManager, Service> serviceFunction) {
            this.serviceFunction = serviceFunction;
        }

        public static Service wrap(Function<ServiceManager, Service> serviceFunction) {
            return new LazyService(serviceFunction);
        }

        public Service load(ServiceManager serviceManager) {
            this.service = serviceFunction.apply(serviceManager);
            return service;
        }

        public boolean isLoaded() {
            return this.service != null;
        }

        public Service service() {
            if (service == null) {
                throw new IllegalStateException("Service must be loaded before being accessed");
            }
            return service;
        }
    }
}
