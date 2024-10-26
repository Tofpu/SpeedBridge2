package io.tofpu.speedbridge2.model.player.exception;

import java.util.UUID;

public class PlayerDatabaseExceptionWrapper extends RuntimeException {
    public PlayerDatabaseExceptionWrapper(final String message, final UUID uniqueId,
                                          final Throwable e) {
        super(message + ": " + uniqueId.toString(), e);
    }
}
