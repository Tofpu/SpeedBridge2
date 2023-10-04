package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.bridge.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.schematic.SchematicResolver;

import java.util.function.Predicate;

public interface ArenaAdapter {
    static ArenaAdapter simple(World gameWorld, ClipboardPaster clipboardPaster, SchematicResolver schematicResolver, Predicate<String> schematicPredicate) {
        return new SimpleArenaAdapter(gameWorld, clipboardPaster, schematicResolver, schematicPredicate);
    }

    void resetAndLoadGameWorld();

    World gameWorld();
    ClipboardPaster clipboardPaster();
    SchematicResolver schematicResolver();

    Predicate<String> schematicPredicate();

    class SimpleArenaAdapter implements ArenaAdapter {
        private final World gameWorld;
        private final ClipboardPaster clipboardPaster;
        private final SchematicResolver schematicResolver;
        private final Predicate<String> schematicPredicate;

        public SimpleArenaAdapter(World gameWorld, ClipboardPaster clipboardPaster, SchematicResolver schematicResolver, Predicate<String> schematicPredicate) {
            this.gameWorld = gameWorld;
            this.clipboardPaster = clipboardPaster;
            this.schematicResolver = schematicResolver;
            this.schematicPredicate = schematicPredicate;
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

        @Override
        public Predicate<String> schematicPredicate() {
            return schematicPredicate;
        }
    }
}
