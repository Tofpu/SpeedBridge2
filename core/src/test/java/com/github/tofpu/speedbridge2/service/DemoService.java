package com.github.tofpu.speedbridge2.service;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoService implements Service {

    private final AtomicInteger counter = new AtomicInteger();

    public void increment() {
        counter.incrementAndGet();
    }

    public int get() {
        return counter.get();
    }
}
