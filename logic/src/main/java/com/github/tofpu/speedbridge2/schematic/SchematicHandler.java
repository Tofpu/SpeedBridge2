package com.github.tofpu.speedbridge2.schematic;

import com.github.tofpu.speedbridge2.object.Vector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SchematicHandler {
    private final File schematicFolder;
    private final SchematicResolver schematicResolver;

    private final Map<String, Schematic> resolvedSchematics = new HashMap<>();

    public SchematicHandler(File schematicFolder, SchematicResolver schematicResolver) {
        this.schematicFolder = schematicFolder;
        this.schematicResolver = schematicResolver;
    }

    public Schematic resolve(final String schematicName) {
        if (resolvedSchematics.containsKey(schematicName)) {
            return getSafe(schematicName);
        }
        File resolvedFile = resolveFile(schematicName);
        if (!resolvedFile.exists()) {
            throw new RuntimeException("There is no schematic at this specified path: " + resolvedFile);
        }

        Vector origin = schematicResolver.origin(resolvedFile);
        Schematic schematic = new Schematic(resolvedFile, origin);

        this.resolvedSchematics.put(schematicName, schematic);
        return schematic;
    }

    public Schematic getSafe(final String schematicName) {
        Schematic schematic = resolvedSchematics.get(schematicName);
        if (schematic == null) {
            throw new RuntimeException("There is no schematic with the name: " + schematicName);
        }
        return schematic;
    }

    private File resolveFile(final String schematicName) {
        return new File(schematicFolder, schematicName);
    }
}
