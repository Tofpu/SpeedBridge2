package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.bridge.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.schematic.SchematicResolver;

public interface ArenaAdapter {
    static ArenaAdapter simple(World world, ClipboardPaster clipboardPaster, SchematicResolver schematicResolver) {
        return new SimpleArenaAdapter(world, clipboardPaster, schematicResolver);
    }

    void resetAndLoadGameWorld();

    World gameWorld();
    ClipboardPaster clipboardPaster();
    SchematicResolver schematicResolver();

    class SimpleArenaAdapter implements ArenaAdapter {
        private final World gameWorld;
        private final ClipboardPaster clipboardPaster;
        private final SchematicResolver schematicResolver;

        public SimpleArenaAdapter(World gameWorld, ClipboardPaster clipboardPaster, SchematicResolver schematicResolver) {
            this.gameWorld = gameWorld;
            this.clipboardPaster = clipboardPaster;
            this.schematicResolver = schematicResolver;
        }

        @Override
        public void resetAndLoadGameWorld() {

        }

        @Override
        public World gameWorld() {
            return gameWorld;
        }

        @Override
        public ClipboardPaster clipboardPaster() {
            return clipboardPaster;
        }

        @Override
        public SchematicResolver schematicResolver() {
            return schematicResolver;
        }
    }
}
