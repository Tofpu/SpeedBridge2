package com.github.tofpu.speedbridge2.common;

import com.github.tofpu.speedbridge2.common.gameextra.ClipboardPaster;
import com.github.tofpu.speedbridge2.common.schematic.SchematicResolver;
import com.github.tofpu.speedbridge2.object.World;

import java.util.function.Predicate;

public interface PlatformArenaAdapter {
    static PlatformArenaAdapter simple(World gameWorld, ClipboardPaster clipboardPaster, SchematicResolver schematicResolver, Predicate<String> schematicPredicate) {
        return new SimpleArenaAdapter(gameWorld, clipboardPaster, schematicResolver, schematicPredicate);
    }

    static PlatformArenaAdapter simple(World gameWorld, ClipboardPaster clipboardPaster, SchematicResolver schematicResolver) {
        return simple(gameWorld, clipboardPaster, schematicResolver, s -> false);
    }

    static PlatformArenaAdapter simple(World gameWorld) {
        return simple(gameWorld, ClipboardPaster.empty(), SchematicResolver.empty());
    }

    void resetAndLoadGameWorld();

    World gameWorld();
    ClipboardPaster clipboardPaster();
    SchematicResolver schematicResolver();

    Predicate<String> schematicPredicate();

    class SimpleArenaAdapter implements PlatformArenaAdapter {
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
