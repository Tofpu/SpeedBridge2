package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.game.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.object.World;

public interface ArenaAdapter {
    static ArenaAdapter simple(World world, ClipboardPaster clipboardPaster) {
        return new SimpleArenaAdapter(world, clipboardPaster);
    }

    void resetGameWorld();

    World gameWorld();
    ClipboardPaster clipboardPaster();

    class SimpleArenaAdapter implements ArenaAdapter {
        private final World gameWorld;
        private final ClipboardPaster clipboardPaster;

        public SimpleArenaAdapter(World gameWorld, ClipboardPaster clipboardPaster) {
            this.gameWorld = gameWorld;
            this.clipboardPaster = clipboardPaster;
        }

        @Override
        public void resetGameWorld() {

        }

        @Override
        public World gameWorld() {
            return gameWorld;
        }

        @Override
        public ClipboardPaster clipboardPaster() {
            return clipboardPaster;
        }
    }
}
