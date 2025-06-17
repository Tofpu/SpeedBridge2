package io.tofpu.speedbridge2.setup.infra.listener.indication;

import io.tofpu.speedbridge2.arena.Vector;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.function.Consumer;

record TargetAxis(
        AxisType type,
        Vector minimumPoint, Vector maximumPoint
) {
    void forEach(World world, Consumer<Location> consumer) {
        for (double value = type.get(minimumPoint); value < type.get(maximumPoint); value++) {
            Axis axis = switch (type) {
                case X -> Axis.x(value);
                case Y -> Axis.y(value);
                case Z -> Axis.z(value);
            };
            axis.sides(world, minimumPoint, maximumPoint).forEach(consumer);
        }
    }
}
