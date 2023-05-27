package com.github.tofpu.speedbridge2.listener.dispatcher;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.listener.Event;
import com.github.tofpu.speedbridge2.listener.Listener;
import com.github.tofpu.speedbridge2.listener.dispatcher.invoker.ListenerInvoker;
import com.github.tofpu.speedbridge2.listener.dispatcher.invoker.MethodInvoker;
import com.github.tofpu.speedbridge2.service.Service;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EventDispatcherService implements Service {

    private final Map<Class<? extends Event>, List<ListenerInvoker>> listenerMap = new HashMap<>();
    private final Set<Class<Listener>> registeredListeners = new HashSet<>();

    @NotNull
    @SuppressWarnings("unchecked")
    private static Map<Class<Event>, Method> getMethodMap(final Class<? extends Listener> listenerClass) {
        return Arrays.stream(listenerClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(EventListener.class)
                && method.getParameterCount() == 1 && Event.class.isAssignableFrom(
                method.getParameterTypes()[0]))
            .collect(Collectors.toMap(k -> (Class<Event>) k.getParameterTypes()[0], v -> v));
    }

    public void dispatch(final Event event) {
        Class<? extends Event> eventClass = event.getClass();
        requireState(isRegisteredEvent(eventClass), "There is no listener listening for event %s.",
            eventClass.getSimpleName());

        this.listenerMap.get(eventClass).forEach(listenerInvoker -> listenerInvoker.invoke(event));
    }

    public boolean isRegisteredEvent(Class<? extends Event> eventClass) {
        return this.listenerMap.containsKey(eventClass);
    }

    @SuppressWarnings("unchecked")
    public void register(final Listener listener) {
        Class<Listener> listenerClass = (Class<Listener>) listener.getClass();
        requireState(!isRegisteredListener(listenerClass),
            "There is already a registered %s listener.",
            listenerClass.getSimpleName());

        Map<Class<Event>, Method> eventMap = getMethodMap(listenerClass);

        eventMap.forEach((aClass, method) -> {
            MethodInvoker invoker = new MethodInvoker(listener, method);
            this.listenerMap.compute(aClass, (aClass1, invokers) -> {
                if (invokers == null) {
                    invokers = new ArrayList<>();
                }
                invokers.add(invoker);
                return invokers;
            });
        });
        this.registeredListeners.add(listenerClass);
    }

    public void unregister(final Class<? extends Listener> listenerClass) {
        requireState(isRegisteredListener(listenerClass), "There is no registered listener of %s.",
            listenerClass.getSimpleName());
        getMethodMap(listenerClass).keySet().forEach(eventClass -> this.listenerMap.get(eventClass)
            .removeIf(listenerInvoker -> listenerInvoker.name().equals(listenerInvoker.name())));
    }

    public boolean isRegisteredListener(final Class<?> clazz) {
        return this.registeredListeners.contains(clazz);
    }
}
