package io.tofpu.speedbridge2.setup.infra.listener.indication;

import io.tofpu.speedbridge2.arena.Vector;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public record AxisSide(AxisSideTypes sideTypes, AxisType mainType, double mainValue) {
    public Collection<Vector> sides(Vector minimumPoint, Vector maximumPoint) {
        double minFirst = sideTypes.first().get(minimumPoint);
        double minSecond = sideTypes.second().get(minimumPoint);

        double maxFirst = sideTypes.first().get(maximumPoint);
        double maxSecond = sideTypes.second().get(maximumPoint);

        return Set.of(
                createVector(Map.of(
                        mainType, mainValue,
                        sideTypes.first(), minFirst,
                        sideTypes.second(), minSecond
                )), // min-min
                createVector(Map.of(
                        mainType, mainValue,
                        sideTypes.first(), maxFirst,
                        sideTypes.second(), maxSecond
                )), // max-max

                createVector(Map.of(
                        mainType, mainValue,
                        sideTypes.first(), maxFirst,
                        sideTypes.second(), minSecond
                )), // max-min
                createVector(Map.of(
                        mainType, mainValue,
                        sideTypes.first(), minFirst,
                        sideTypes.second(), maxSecond
                )) // min-max
        );
    }

    Vector createVector(Map<AxisType, Double> values) {
        return new Vector(
                values.getOrDefault(AxisType.X, 0.0),
                values.getOrDefault(AxisType.Y, 0.0),
                values.getOrDefault(AxisType.Z, 0.0)
        );
    }
}
