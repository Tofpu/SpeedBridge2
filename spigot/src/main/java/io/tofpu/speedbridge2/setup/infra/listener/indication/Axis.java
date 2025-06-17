package io.tofpu.speedbridge2.setup.infra.listener.indication;

import io.tofpu.speedbridge2.arena.Vector;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collection;
import java.util.Map;

public interface Axis {
    static Axis x(double x) {
        return new X(x);
    }

    static Axis y(double y) {
        return new Y(y);
    }

    static Axis z(double z) {
        return new Z(z);
    }

    double value();

    Location toLocation(World world, Vector vector);

    AxisType type();

    Pair<AxisType> sides();
    default AxisSideTypes sideTypes() {
        Pair<AxisType> sides = sides();
        return new AxisSideTypes(sides.first, sides.second);
    }

    default Collection<Location> sides(World world, Vector minimumPoint, Vector maximumPoint) {
        AxisSide axisSide = new AxisSide(sideTypes(), type(), value());
        return axisSide.sides(minimumPoint, maximumPoint).stream()
                .map(vector -> toLocation(world, vector))
                .toList();
    }

    // y has sides of x and z
//    default Collection<Location> sides(World world, Vector minimumPoint, Vector maximumPoint) {
//        Set<Location> result = new HashSet<>();
//        result.add(toLocation(world, minimumPoint)); // min
//        result.add(toLocation(world, maximumPoint)); // max
//
//        Pair<AxisType> sides = sides();
//
//        AxisType first = sides.first;
//        double minFirst = first.get(minimumPoint);
//        double maxFirst = first.get(maximumPoint);
//
//        AxisType second = sides.second;
//        double minSecond = second.get(minimumPoint);
//        double maxSecond = second.get(maximumPoint);
//
//        // a mixture of min and max for both sides of the axis
//        result.add(createLoc(world, Map.of(
//                type(), value(),
//                first, maxFirst,
//                second, minSecond
//        )));
//        result.add(createLoc(world, Map.of(
//                type(), value(),
//                first, minFirst,
//                second, maxSecond
//        )));
//
//        return result;
//    }

    static Location createLoc(World world, Map<AxisType, Double> values) {
        double x = values.getOrDefault(AxisType.X, 0.0);
        double y = values.getOrDefault(AxisType.Y, 0.0);
        double z = values.getOrDefault(AxisType.Z, 0.0);
        return new Location(world, x, y, z);
    }

    record Pair<T>(T first, T second) {
        public static <T> Pair<T> of(T first, T second) {
            return new Pair<>(first, second);
        }
    }

    record X(double x) implements Axis {
        @Override
        public double value() {
            return x;
        }

        public Location toLocation(World world, Vector vector) {
            return new Location(world, x, vector.y(), vector.z());
        }

        @Override
        public AxisType type() {
            return AxisType.X;
        }

        @Override
        public Pair<AxisType> sides() {
            return Pair.of(
                    AxisType.Y, AxisType.Z
            );
        }
    }

    record Y(double y) implements Axis {
        @Override
        public double value() {
            return y;
        }

        public Location toLocation(World world, Vector vector) {
            return new Location(world, vector.x(), y, vector.z());
        }

        @Override
        public AxisType type() {
            return AxisType.Y;
        }

        @Override
        public Pair<AxisType> sides() {
            return Pair.of(
                    AxisType.X, AxisType.Z
            );
        }
    }

    record Z(double z) implements Axis {
        @Override
        public double value() {
            return z;
        }

        public Location toLocation(World world, Vector vector) {
            return new Location(world, vector.x(), vector.y(), z);
        }

        @Override
        public AxisType type() {
            return AxisType.Z;
        }

        @Override
        public Pair<AxisType> sides() {
            return Pair.of(
                    AxisType.X, AxisType.Y
            );
        }
    }
}
