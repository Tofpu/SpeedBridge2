package io.tofpu.speedbridge2.model.player.exception;

import java.util.UUID;

public final class PlayerLoadFailureException extends PlayerDatabaseExceptionWrapper {
    public PlayerLoadFailureException(final UUID uniqueId, final Throwable e) {
        super("Failed to load the player data", uniqueId, e);
    }
}
