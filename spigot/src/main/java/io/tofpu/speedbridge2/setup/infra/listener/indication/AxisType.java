package io.tofpu.speedbridge2.setup.infra.listener.indication;

import io.tofpu.speedbridge2.arena.Vector;

import java.util.EnumSet;

public enum AxisType {
    X {
        @Override
        public double get(Vector vector) {
            return vector.x();
        }
    }, Y {
        @Override
        public double get(Vector vector) {
            return vector.y();
        }
    }, Z {
        @Override
        public double get(Vector vector) {
            return vector.z();
        }
    };

    public static final EnumSet<AxisType> ALL = EnumSet.allOf(AxisType.class);

    public abstract double get(Vector vector);
}
