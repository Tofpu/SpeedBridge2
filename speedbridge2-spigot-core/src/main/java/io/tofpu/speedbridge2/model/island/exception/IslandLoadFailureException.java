package io.tofpu.speedbridge2.model.island.exception;

public final class IslandLoadFailureException extends IslandDatabaseExceptionWrapper {
    public IslandLoadFailureException(final Throwable e) {
        super("Failed to load the island data", -1, e);
    }
}
