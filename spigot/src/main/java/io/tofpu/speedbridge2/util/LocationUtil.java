package io.tofpu.speedbridge2.util;

import io.tofpu.speedbridge2.arena.Vector;
import org.bukkit.Location;

public class LocationUtil {
    public static Vector asVector(Location location) {
        return new Vector(location.getX(), location.getY(), location.getZ());
    }
}
