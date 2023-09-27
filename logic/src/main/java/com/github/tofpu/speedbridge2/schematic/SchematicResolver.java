package com.github.tofpu.speedbridge2.schematic;

import com.github.tofpu.speedbridge2.object.Vector;

import java.io.File;

public interface SchematicResolver {
    Vector origin(File schematicFile);
}
