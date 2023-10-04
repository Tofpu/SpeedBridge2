package com.github.tofpu.speedbridge2.schematic;

import com.github.tofpu.speedbridge2.object.Vector;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class SchematicHandler {
    private final File schematicFolder;
    private final SchematicResolver schematicResolver;
    private final Predicate<String> schematicPredicate;

    private final Map<String, Schematic> resolvedSchematics = new HashMap<>();

    public static SchematicHandler load(File schematicFolder, SchematicResolver schematicResolver, Predicate<String> schematicPredicate) {
        SchematicHandler schematicHandler = new SchematicHandler(schematicFolder, schematicResolver, schematicPredicate);
        schematicHandler.loadAllSchematics();
        return schematicHandler;
    }

    private SchematicHandler(File schematicFolder, SchematicResolver schematicResolver, Predicate<String> schematicPredicate) {
        this.schematicFolder = schematicFolder;
        this.schematicResolver = schematicResolver;
        this.schematicPredicate = schematicPredicate;
    }

    private void loadAllSchematics() {
        loadSchematics(schematicFolder);
    }

    private void loadSchematics(File directory) {
        for (File schematicFile : directory.listFiles()) {
            if (schematicFile.isDirectory()) {
                loadSchematics(schematicFile);
                continue;
            }
            if (!isSchematic(schematicFile)) {
                continue;
            }
            resolve(schematicFile);
        }
    }

    public Schematic resolve(final String schematicName) {
        if (resolvedSchematics.containsKey(schematicName)) {
            return getSafe(schematicName);
        }
        return resolve(resolveFile(schematicName));
    }

    public Schematic resolve(final File schematicFile) {
        if (!schematicFile.exists()) {
            throw new RuntimeException("There is no schematic at this specified path: " + schematicFile);
        }

        if (!isSchematic(schematicFile)) {
            throw new UnsupportedSchematicType(schematicFile.getName());
        }

        Vector origin = schematicResolver.origin(schematicFile);
        Schematic schematic = new Schematic(schematicFile, origin);

        this.resolvedSchematics.put(FilenameUtils.removeExtension(schematicFile.getName()), schematic);
        return schematic;
    }

    private boolean isSchematic(File schematicFile) {
        return schematicPredicate.test(schematicFile.getName());
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

    public Collection<String> resolvedSchematics() {
        return resolvedSchematics.keySet();
    }
}
