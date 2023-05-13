package com.github.tofpu.speedbridge2.database;

import java.util.function.Consumer;
import org.hibernate.Session;

public interface Database {

    void execute(Consumer<Session> sessionConsumer);

    void shutdown();

    default boolean supportsAsync() {
        return false;
    }
}
