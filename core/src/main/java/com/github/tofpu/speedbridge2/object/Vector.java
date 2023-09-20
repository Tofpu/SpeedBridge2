package com.github.tofpu.speedbridge2.object;

public class Vector {
    private final double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public Vector subtract(Vector vector) {
        return new Vector(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public Vector add(double x, double y, double z) {
        return new Vector(this.x + x, this.y + y, this.z + z);
    }
}
