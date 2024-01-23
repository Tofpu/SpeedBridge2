package com.github.tofpu.speedbridge2.common.message;

import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;

public enum CommonMessages implements ConfigurableMessage {
    MISSING_LOBBY("lobby.missing_lobby", "<red>Please be sure to setup your speedbridge lobby.");

    private final String key;
    private String defaultMessage;

    CommonMessages(String key, String defaultMessage) {
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