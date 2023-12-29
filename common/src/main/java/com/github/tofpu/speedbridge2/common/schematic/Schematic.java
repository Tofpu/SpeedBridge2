package com.github.tofpu.speedbridge2.common.schematic;

import com.github.tofpu.speedbridge2.object.Vector;

import java.io.File;

public class Schematic {
    private final File schematicFile;
    private final Vector originPosition;

    public Schematic(File schematicFile, Vector originPosition) {
        this.schematicFile = schematicFile;
        this.originPosition = originPosition;
    }

    public File schematicFile() {
        return schematicFile;
    }

    public Vector originPosition() {
        return originPosition;
    }
}
