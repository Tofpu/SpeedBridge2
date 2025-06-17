package io.tofpu.speedbridge2.positioning;

import io.tofpu.speedbridge2.util.Position;
import io.tofpu.speedbridge2.util.PositionOrientation;

public record PositionOffset(PositionOrientation offset) {
    public PositionOrientation applyTo(Position origin) {
        return origin.subtract(offset);
    }
}
