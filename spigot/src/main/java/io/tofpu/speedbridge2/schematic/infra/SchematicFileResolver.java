package io.tofpu.speedbridge2.schematic.infra;

import java.io.File;

public class SchematicFileResolver {
    private static final String[] SCHEMATIC_TYPES = {"schematic", "schem"};

    public File resolveSchematicFile(File directory, String name) {
        for (String schematicType : SCHEMATIC_TYPES) {
            File file = new File(directory, name + "." + schematicType);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }
}
