package io.tofpu.speedbridge2.model.player.exception;

import java.util.UUID;

public final class PlayerDeletionFailureException extends PlayerDatabaseExceptionWrapper {
    public PlayerDeletionFailureException(final UUID uniqueId, final Throwable e) {
        super("Failed to delete the player data", uniqueId, e);
    }
}
