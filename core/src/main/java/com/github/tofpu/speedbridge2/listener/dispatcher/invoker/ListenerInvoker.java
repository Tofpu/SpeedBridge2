package com.github.tofpu.speedbridge2.listener.dispatcher.invoker;

import com.github.tofpu.speedbridge2.listener.Event;

public interface ListenerInvoker {
    void invoke(final Event event);
    Class<?> type();
    String name();
}
