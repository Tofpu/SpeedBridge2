package com.github.tofpu.speedbridge2.event.dispatcher;

import com.github.tofpu.speedbridge2.event.Event;
import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.invoker.MethodInvoker;
import com.github.tofpu.speedbridge2.service.Service;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

@SuppressWarnings("unused")
public class EventDispatcherService implements Service {

    private final Map<Class<? extends Event>, Invokers> listenerMap = new HashMap<>();
    private final Set<Class<Listener>> registeredListeners = new HashSet<>();

    @NotNull
    @SuppressWarnings("unchecked")
    private static Map<Class<Event>, Method> getMethodMap(
        final Class<? extends Listener> listenerClass) {
        return Arrays.stream(listenerClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(EventListener.class)
                && method.getParameterCount() == 1 && Event.class.isAssignableFrom(
                method.getParameterTypes()[0]))
            .collect(Collectors.toMap(k -> (Class<Event>) k.getParameterTypes()[0], v -> v));
    }

    public void dispatchIfApplicable(final Event event) {
        Invokers invokers = this.listenerMap.getOrDefault(event.getClass(), Invokers.EMPTY);
        invokers.dispatch(event);
    }

    public void unsafeDispatch(final Event event) {
        Class<? extends Event> eventClass = event.getClass();

        Invokers invokers = this.listenerMap.get(eventClass);
        requireState(invokers != null, "There is no listener listening for event %s.",
            eventClass.getSimpleName());
        invokers.dispatch(event);
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

        Map<Class<Event>, Method> listeningMethods = getMethodMap(listenerClass);

        listeningMethods.forEach((eventClass, method) -> {
            MethodInvoker methodInvoker = new MethodInvoker(listener, method);
            ListenerInvokerWrapper invoker = new ListenerInvokerWrapper(methodInvoker, method);
            this.listenerMap.computeIfAbsent(eventClass, aClass -> new Invokers()).add(invoker);
        });
        this.registeredListeners.add(listenerClass);
    }

    public void unregister(final Class<? extends Listener> listenerClass) {
        requireState(isRegisteredListener(listenerClass), "There is no registered listener of %s.",
            listenerClass.getSimpleName());

        getMethodMap(listenerClass).keySet().forEach(eventClass -> this.listenerMap.get(eventClass)
            .removeIf(listenerInvoker -> listenerInvoker.name().equals(listenerInvoker.name())));
        this.registeredListeners.remove(listenerClass);

        requireState(!containListenerInternally(listenerClass),
            "Failed to unregister %s listener properly!", listenerClass.getSimpleName());
        requireState(!isRegisteredListener(listenerClass),
            "Failed to unregister %s listener properly!", listenerClass.getSimpleName());
    }

    public boolean isRegisteredListener(final Class<?> clazz) {
        return this.registeredListeners.contains(clazz);
    }

    private boolean containListenerInternally(final Class<? extends Listener> listenerClass) {
        AtomicBoolean contains = new AtomicBoolean(false);

        getMethodMap(listenerClass).keySet().forEach(eventClass -> {
            Invokers invokers = this.listenerMap.get(eventClass);
            if (invokers == null) return;

            for (String invokerListenerName : invokers.listenerNames()) {
                System.out.println(invokerListenerName + " vs " + listenerClass);
                if (listenerClass.getSimpleName().equals(invokerListenerName)) {
                    contains.set(true);
                    break;
                }
            }
        });

        return contains.get();
    }
}
