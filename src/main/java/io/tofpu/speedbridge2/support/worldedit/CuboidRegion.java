package io.tofpu.speedbridge2.support.worldedit;

import io.tofpu.multiworldedit.VectorWrapper;

public final class CuboidRegion {
  private final VectorWrapper minVector, maxVector;

  public CuboidRegion(final VectorWrapper minVector, final VectorWrapper maxVector) {
    this.minVector = minVector;
    this.maxVector = maxVector;
  }

  public Vector getMinimumPoint() {
    return new Vector(
        Math.min(minVector.getX(), maxVector.getX()),
        Math.min(minVector.getY(), maxVector.getY()),
        Math.min(minVector.getZ(), maxVector.getZ()));
  }

  public Vector getMaximumPoint() {
    return new Vector(
        Math.max(minVector.getX(), maxVector.getX()),
        Math.max(minVector.getY(), maxVector.getY()),
        Math.max(minVector.getZ(), maxVector.getZ()));
  }

  public VectorWrapper getMinVector() {
    return minVector;
  }

  public VectorWrapper getMaxVector() {
    return maxVector;
  }

  public boolean contains(final org.bukkit.util.Vector vector) {
    final double x = vector.getX();
    final double y = vector.getY();
    final double z = vector.getZ();

    return contains(new Vector(x, y, z));
  }

  public boolean contains(final Vector vector) {
    final double x = vector.getX();
    final double y = vector.getY();
    final double z = vector.getZ();

    return x >= minVector.getX()
        && x <= maxVector.getX()
        && y >= minVector.getY()
        && y <= maxVector.getY()
        && z >= minVector.getZ()
        && z <= maxVector.getZ();
  }
}
