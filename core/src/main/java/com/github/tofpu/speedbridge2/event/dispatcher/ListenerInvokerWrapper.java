package com.github.tofpu.speedbridge2.event.dispatcher;

import com.github.tofpu.speedbridge2.event.Cancellable;
import com.github.tofpu.speedbridge2.event.Event;
import com.github.tofpu.speedbridge2.event.dispatcher.invoker.EventInvoker;
import com.github.tofpu.speedbridge2.event.dispatcher.invoker.MethodInvoker;

import java.lang.reflect.Method;

public class ListenerInvokerWrapper implements EventInvoker {
    private final EventInvoker delegate;
    private final EventListener eventListener;

    public ListenerInvokerWrapper(MethodInvoker delegate, Method method) {
        this.delegate = delegate;
        this.eventListener = method.getAnnotation(EventListener.class);
    }

    @Override
    public void invoke(Event event) {
        if (isEventCancelled(event) && eventListener.ignoreCancelled()) {
            return;
        }
        delegate.invoke(event);
    }

    @Override
    public Class<?> type() {
        return delegate.type();
    }

    @Override
    public String name() {
        return delegate.name();
    }

    private boolean isEventCancelled(Event event) {
        return event instanceof Cancellable && ((Cancellable) event).cancelled();
    }

    public EventListener eventListener() {
        return eventListener;
    }
}
