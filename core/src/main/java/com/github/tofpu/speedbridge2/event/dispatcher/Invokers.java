package com.github.tofpu.speedbridge2.event.dispatcher;

import com.github.tofpu.speedbridge2.event.Event;
import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.invoker.MethodInvoker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Invokers {
    public static final Invokers EMPTY = new Invokers();

    private final List<MethodInvoker> listeningMethods = new ArrayList<>();
    private final List<MethodInvoker> monitoringMethods = new ArrayList<>();

    public void add(MethodInvoker methodInvoker) {
        EventListener annotation = methodInvoker.method().getAnnotation(EventListener.class);
        ListeningState state = annotation.state();
        if (state == ListeningState.LISTENING) {
            listeningMethods.add(methodInvoker);
        } else if (state == ListeningState.MONITORING) {
            monitoringMethods.add(methodInvoker);
        } else {
            throw new IllegalStateException(String.format("Unsupported listening state: %s", state));
        }
    }

    public void dispatch(Event event) {
        listeningMethods.forEach(methodInvoker -> methodInvoker.invoke(event));
        monitoringMethods.forEach(methodInvoker -> methodInvoker.invoke(event));
    }

    public void removeIf(Predicate<MethodInvoker> filter) {
        listeningMethods.removeIf(filter);
        monitoringMethods.removeIf(filter);
    }

    public Collection<String> listenerNames() {
        List<String> names = new ArrayList<>();
        listeningMethods.forEach(methodInvoker -> names.add(methodInvoker.name()));
        monitoringMethods.forEach(methodInvoker -> names.add(methodInvoker.name()));
        return names;
    }

    public void forEach(Consumer<MethodInvoker> action) {
        listeningMethods.forEach(action);
        monitoringMethods.forEach(action);
    }
}
