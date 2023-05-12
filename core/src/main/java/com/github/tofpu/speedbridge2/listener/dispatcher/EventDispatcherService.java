package com.github.tofpu.speedbridge2.listener.dispatcher;

import com.github.tofpu.speedbridge2.listener.Event;
import com.github.tofpu.speedbridge2.listener.Listener;
import com.github.tofpu.speedbridge2.service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

@SuppressWarnings("unused")
public class EventDispatcherService implements Service {
    private final Map<Class<? extends Event>, Listeners> listenersMap = new HashMap<>();

    public void dispatch(final Event event) {
        requireState(isRegisteredEvent(event.getClass()), "There is no registered listeners with event %s", event.getClass().getSimpleName());
        Listeners listeners = listenersMap.get(event.getClass());
        listeners.dispatch(event);
    }

    @SuppressWarnings("unchecked")
    public void register(final Listener<? extends Event> listener) {
        requireState(!isRegisteredListener((Class<? extends Listener<?>>) listener.getClass()), "%s is already registered as a listener", listener.getClass().getSimpleName());
        this.listenersMap.compute(listener.type(), (event, listeners) -> {
            if (listeners == null) {
                listeners = new Listeners();
            }
            listeners.register(listener);
            return listeners;
        });
    }

    @SuppressWarnings("unchecked")
    public void unregister(final Listener<?> listener) {
        requireState(isRegisteredListener((Class<? extends Listener<?>>) listener.getClass()), "There is no registered %s listener", listener.getClass().getSimpleName());
        this.listenersMap.compute(listener.type(), (event, listeners) -> {
            assert listeners != null;
            listeners.unregister(listener);
            return listeners;
        });
    }

    public boolean isRegisteredEvent(final Class<? extends Event> clazz) {
        return this.listenersMap.containsKey(clazz);
    }

    public boolean isRegisteredListener(final Class<? extends Listener<?>> clazz) {
        return this.listenersMap.values().stream().anyMatch(listeners -> listeners.isRegistered(clazz));
    }

    private static class Listeners {
        private final List<Listener<?>> registeredListeners = new ArrayList<>();

        public void register(final Listener<?> listener) {
            this.registeredListeners.add(listener);
        }

        public void unregister(final Listener<?> listener) {
            this.registeredListeners.remove(listener);
        }

        public boolean isRegistered(final Class<? extends Listener<?>> listenerClazz) {
            return registeredListeners.stream().anyMatch(listener -> listener.getClass().equals(listenerClazz));
        }

        public void dispatch(Event event) {
            for (Listener<?> listener : this.registeredListeners) {
                listener.handle(event);
            }
        }
    }
}
