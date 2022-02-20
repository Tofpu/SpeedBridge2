package io.tofpu.speedbridge2.support.worldedit;

public final class CuboidRegion {
    private final Vector minVector, maxVector;

    public CuboidRegion(final Vector minVector, final Vector maxVector) {
        this.minVector = minVector;
        this.maxVector = maxVector;
    }

    public Vector getMinimumPoint() {
        return new Vector(Math.min(minVector.getX(), maxVector.getX()), Math.min(minVector.getY(), maxVector.getY()), Math.min(minVector.getZ(), maxVector.getZ()));
    }

    public Vector getMaximumPoint() {
        return new Vector(Math.max(minVector.getX(), maxVector.getX()), Math.max(minVector.getY(), maxVector.getY()), Math.max(minVector.getZ(), maxVector.getZ()));
    }

    public Vector getMinVector() {
        return minVector;
    }

    public Vector getMaxVector() {
        return maxVector;
    }

    public boolean contains(final Vector vector) {
        final double x = vector.getX();
        final double y = vector.getY();
        final double z = vector.getZ();

        return x >= minVector.getX() && x <= maxVector.getX() && y >= minVector.getY() &&
               y <= maxVector.getY() && z >= minVector.getZ() && z <= maxVector.getZ();
    }
}
