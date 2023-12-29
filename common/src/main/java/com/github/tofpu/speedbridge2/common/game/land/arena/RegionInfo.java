package com.github.tofpu.speedbridge2.common.game.land.arena;

import com.github.tofpu.speedbridge2.object.Vector;
import lombok.Data;

@Data
public class RegionInfo {
    private final int width, height;
    private final Vector origin, minimumPoint, maximumPoint;

    public RegionInfo(int width, int height, Vector origin, Vector minimumPoint, Vector maximumPoint) {
        this.width = width;
        this.height = height;
        this.origin = origin;
        this.minimumPoint = minimumPoint;
        this.maximumPoint = maximumPoint;
    }
}
