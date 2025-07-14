package io.tofpu.speedbridge2.schematic.infra;

import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.speedbridge2.schematic.domain.Schematic;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SchematicHandler {
    private final File schematicDirectory;
    private final SchematicFileResolver fileResolver;
    private final ClipboardResolver clipboardResolver;

    private final Map<String, Schematic> schematics = new HashMap<>();

    public SchematicHandler(File schematicDirectory) {
        this.schematicDirectory = schematicDirectory;
        this.fileResolver = new SchematicFileResolver();
        this.clipboardResolver = new ClipboardResolver();
    }

    public Schematic resolveSchematic(String name) {
        return schematics.computeIfAbsent(name, s -> {
            File file = resolveSchematicFile(name);
            if (file == null) {
                return null;
            }

            ClipboardWrapper clipboardWrapper = clipboardResolver.resolveClipboard(file);
            if (clipboardWrapper == null) {
                return null;
            }
            return new Schematic(name, file, clipboardWrapper);
        });
    }

    private File resolveSchematicFile(String name) {
        return fileResolver.resolveSchematicFile(schematicDirectory, name);
    }

    public Collection<String> schematicNames() {
        return Collections.unmodifiableCollection(schematics.keySet());
    }

    public boolean contains(String schematicName) {
        return schematics.containsKey(schematicName);
    }
}
