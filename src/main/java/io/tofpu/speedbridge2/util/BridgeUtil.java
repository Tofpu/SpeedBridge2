package io.tofpu.speedbridge2.util;

import org.bukkit.util.Vector;

public final class BridgeUtil {
    public static Vector toVector(final com.sk89q.worldedit.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static String toFormattedScore(final double score) {
        return String.format("%.3f", score);
    }
}
