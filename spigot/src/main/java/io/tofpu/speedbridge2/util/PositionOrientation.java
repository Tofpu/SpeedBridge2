package io.tofpu.speedbridge2.util;

import org.bukkit.Location;
import org.bukkit.World;

public class PositionOrientation {
    private final double x, y, z;
    private final float yaw, pitch;

    public PositionOrientation(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    public PositionOrientation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PositionOrientation add(Position other) {
        return new PositionOrientation(x + other.getX(), y + other.getY(), z + other.getZ(), yaw, pitch);
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
