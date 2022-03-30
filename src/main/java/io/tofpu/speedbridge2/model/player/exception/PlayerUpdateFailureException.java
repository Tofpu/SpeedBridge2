package io.tofpu.speedbridge2.model.player.exception;

import java.util.UUID;

public final class PlayerUpdateFailureException extends PlayerDatabaseExceptionWrapper {
    public PlayerUpdateFailureException(final UUID uniqueId, final Throwable e) {
        super("Failed to update the player data", uniqueId, e);
    }
}
