package io.tofpu.speedbridge2.command;

import java.util.UUID;

public class NameAndUUID {
    private final String playerName;
    private final UUID playerUUID;

    public NameAndUUID(String playerName, UUID playerUUID) {
        this.playerName = playerName;
        this.playerUUID = playerUUID;
    }

    public String playerName() {
        return playerName;
    }

    public UUID playerUUID() {
        return playerUUID;
    }
}
