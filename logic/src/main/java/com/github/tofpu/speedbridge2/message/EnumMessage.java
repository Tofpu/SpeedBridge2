package com.github.tofpu.speedbridge2.message;

import com.github.tofpu.speedbridge2.player.ConfigurableMessage;

public enum EnumMessage implements ConfigurableMessage {
    MISSING_LOBBY {
        @Override
        public String key() {
            return "lobby.missing_lobby";
        }

        @Override
        public String defaultMessage() {
            return "<red>Please be sure to setup your speedbridge lobby.";
        }
    };

    EnumMessage() {}
}