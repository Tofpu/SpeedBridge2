package io.tofpu.speedbridge2.model.island.exception;

public class IslandDatabaseExceptionWrapper extends RuntimeException {
    public IslandDatabaseExceptionWrapper(final String message, final int slot,
                                          final Throwable e) {
        super(message + (slot == -1 ? "" : ": " + slot), e);
    }
}
