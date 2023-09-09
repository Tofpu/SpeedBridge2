package com.github.tofpu.speedbridge2.game.core.arena;

import com.github.tofpu.speedbridge2.game.island.arena.RegionInfo;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public abstract class ClipboardPaster {
    public static ClipboardPaster empty() {
        return new EmptyClipboardPaster();
    }

    public abstract RegionInfo getRegion(File file);
    public abstract void paste(File schematicFile, Position position);

    static class EmptyClipboardPaster extends ClipboardPaster {
        @Override
        public RegionInfo getRegion(File file) {
            return new RegionInfo(randomInt(), randomInt(), randomVector(), randomVector(), randomVector());
        }

        private Vector randomVector() {
            return new Vector(randomInt(), randomInt(), randomInt());
        }

        private static int randomInt() {
            return ThreadLocalRandom.current().nextInt(10, 100);
        }

        @Override
        public void paste(File schematicFile, Position position) {
            // does nothing
        }
    }
}
