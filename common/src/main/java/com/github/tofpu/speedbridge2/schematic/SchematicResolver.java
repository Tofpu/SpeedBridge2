package com.github.tofpu.speedbridge2.schematic;

import com.github.tofpu.speedbridge2.object.Vector;

import java.io.File;

public interface SchematicResolver {
    static SchematicResolver empty() {
        return new EmptySchematicResolver();
    }

    Vector origin(File schematicFile);

    class EmptySchematicResolver implements SchematicResolver {
        @Override
        public Vector origin(File schematicFile) {
            return new Vector(0, 0, 0);
        }
    }
}
