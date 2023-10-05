package com.github.tofpu.speedbridge2.adapter;

import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.World;
import io.tofpu.multiworldedit.VectorWrapper;

public class BukkitAdapter {

    public static World toWorld(org.bukkit.World world) {
        if (world == null) return null;
        return new World(world.getName());
    }

    public static Location toLocation(org.bukkit.Location location) {
        return new Location(toWorld(location.getWorld()), (int) location.getX(),
                (int) location.getY(),
                (int) location.getZ(), location.getYaw(), location.getPitch());
    }

    public static Position toPosition(org.bukkit.Location location) {
        return new Position(toWorld(location.getWorld()), (int) location.getX(),
                (int) location.getY(),
                (int) location.getZ());
    }

    public static Vector toVector(org.bukkit.util.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector toVector(VectorWrapper vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }
}
