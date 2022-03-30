package io.tofpu.speedbridge2.model.player.exception;

import java.util.UUID;

public final class PlayerUpdateNameFailureException extends PlayerDatabaseExceptionWrapper {
    public PlayerUpdateNameFailureException(final UUID uniqueId, final Throwable e) {
        super("Failed to update the player name", uniqueId, e);
    }
}
