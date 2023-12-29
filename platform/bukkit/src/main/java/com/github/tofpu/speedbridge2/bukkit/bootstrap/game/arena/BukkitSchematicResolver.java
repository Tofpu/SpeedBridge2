package com.github.tofpu.speedbridge2.bukkit.bootstrap.game.arena;

import com.github.tofpu.speedbridge2.bukkit.adapter.BukkitAdapter;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.common.schematic.SchematicResolver;
import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.MultiWorldEdit;

import java.io.File;

public class BukkitSchematicResolver implements SchematicResolver {
    private final MultiWorldEdit multiWorldEdit;

    public BukkitSchematicResolver(MultiWorldEdit multiWorldEdit) {
        this.multiWorldEdit = multiWorldEdit;
    }

    @Override
    public Vector origin(File schematicFile) {
        ClipboardWrapper clipboardWrapper = multiWorldEdit.read(schematicFile);
        return BukkitAdapter.toVector(clipboardWrapper.getOrigin());
    }
}
