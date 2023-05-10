package com.github.tofpu.speedbridge2.database;

import org.hibernate.Session;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Database {
    void execute(Consumer<Session> sessionConsumer);
    <T> T compute(Function<Session, T> sessionFunction);
    void shutdown();

    default boolean supportsAsync() {
        return false;
    }
}
