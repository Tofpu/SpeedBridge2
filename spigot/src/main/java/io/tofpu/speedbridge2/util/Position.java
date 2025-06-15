package io.tofpu.speedbridge2.util;

import org.bukkit.Location;
import org.bukkit.World;

public record Position(int x, int y, int z) {
    public Position add(Position other) {
        return new Position(x + other.x, y + other.y, z + other.z);
    }

    public Position add(Location other) {
        return new Position(x + other.getBlockX(), y + other.getBlockY(), z + other.getBlockZ());
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }

    public PositionOrientation subtract(PositionOrientation other) {
        return new PositionOrientation(x - other.getX(), y - other.getY(), z - other.getZ(), other.getYaw(), other.getPitch());
    }
}
