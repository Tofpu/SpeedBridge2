package com.github.tofpu.speedbridge2.object.player;

public interface ConfigurableMessage {

    String key();

    String defaultMessage();

    default String defaultMessage(Object... args) {
        return String.format(defaultMessage(), args);
    }

    void setMessage(String content);
}
