package com.github.tofpu.speedbridge2.schematic.exception;

import java.io.File;

public class SchematicNotFoundException extends RuntimeException {
    private final File file;

    public SchematicNotFoundException(File file) {
        super("There is no schematic at this specified path: " + file);
        this.file = file;
    }

    public File file() {
        return file;
    }
}
