package io.tofpu.speedbridge2.game.exception;

public class UnableToFindClipboardException extends RuntimeException {
    public UnableToFindClipboardException(int slot, String schematicName) {
        super(String.format("Unable to find clipboard for island %s with schematic %s", slot, schematicName));
    }
}
