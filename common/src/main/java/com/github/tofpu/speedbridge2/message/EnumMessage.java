package com.github.tofpu.speedbridge2.message;

import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;

public enum EnumMessage implements ConfigurableMessage {
    MISSING_LOBBY("lobby.missing_lobby", "<red>Please be sure to setup your speedbridge lobby.");

    private final String key;
    private String defaultMessage;

    EnumMessage(String key, String defaultMessage) {
        this.key = key;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }

    @Override
    public void setMessage(String content) {
        defaultMessage = content;
    }
}