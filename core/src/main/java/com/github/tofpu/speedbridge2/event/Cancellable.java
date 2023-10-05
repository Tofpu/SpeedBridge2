package com.github.tofpu.speedbridge2.event;

public interface Cancellable {
    void cancel(boolean state);
    boolean cancelled();
}
