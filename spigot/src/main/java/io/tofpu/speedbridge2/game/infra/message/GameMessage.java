package io.tofpu.speedbridge2.game.infra.message;

import io.tofpu.speedbridge2.util.message.MessageKey;

public enum GameMessage implements MessageKey {
    GAME_JOINED("game.joined", "game"),
    ALREADY_IN_GAME("game.alreadyInGame", "game"),
    GAME_LEFT("game.left", "game"),
    NOT_IN_GAME("game.notInGame", "game");

    private final String key;
    private final String component;

    GameMessage(String key, String component) {
        this.key = key;
        this.component = component;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String component() {
        return component;
    }
}
