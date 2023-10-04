package com.github.tofpu.speedbridge2.schematic;

public class UnsupportedSchematicType extends RuntimeException {
    public UnsupportedSchematicType(String fileName) {
        super("Unsupported schematic type: " + fileName);
    }
}
