package com.github.tofpu.speedbridge2.common.gameextra.land.object;

import com.github.tofpu.speedbridge2.object.Vector;

public class CuboidRegion {
    private final Vector minVector, maxVector;

    public CuboidRegion(final Vector minVector, final Vector maxVector) {
        this.minVector = minVector;
        this.maxVector = maxVector;
    }

    public boolean contains(final Vector vector) {
        final double x = vector.x();
        final double y = vector.y();
        final double z = vector.z();

        return x >= minVector.x() && x <= maxVector.x() && y >= minVector.y() &&
                y <= maxVector.y() && z >= minVector.z() && z <= maxVector.z();
    }
}
