package io.tofpu.speedbridge2.arena;

public record Vector(double x, double y, double z) {
    public Vector add(double x, double y, double z) {
        return new Vector(this.x + x, this.y + y, this.z + z);
    }

    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }
}