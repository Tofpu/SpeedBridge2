package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.game.land.arena.RegionInfo;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.World;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public abstract class ClipboardPaster {
    public static ClipboardPaster empty() {
        return new EmptyClipboardPaster();
    }

    public abstract RegionInfo getRegion(File file);
    public abstract void paste(File schematicFile, Position position);

    public abstract void clear(Vector minPoint, Vector maxPoint, World world);

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

        @Override
        public void clear(Vector minPoint, Vector maxPoint, World world) {
            // does nothing
        }
    }
}
