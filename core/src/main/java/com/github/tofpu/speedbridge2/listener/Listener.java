package com.github.tofpu.speedbridge2.listener;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireArgument;

public abstract class Listener<E extends Event> {
    protected abstract void on(final E event);
    public void handle(final Event event) {
        Class<? extends Event> eventClass = event.getClass();
        requireArgument(eventClass.isAssignableFrom(type()), "Event %s is not assignable from %s", eventClass, type());
        on((E) event);
    }
    public abstract Class<E> type();
}
