package com.github.tofpu.speedbridge2.common.schematic.exception;

import java.io.File;

public class UnsupportedSchematicType extends RuntimeException {
    private final File file;

    public UnsupportedSchematicType(File file) {
        super("Unsupported schematic type: " + file);
        this.file = file;
    }

    public File file() {
        return file;
    }
}
