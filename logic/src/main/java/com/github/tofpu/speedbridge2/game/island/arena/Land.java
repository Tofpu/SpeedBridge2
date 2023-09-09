package com.github.tofpu.speedbridge2.game.island.arena;

import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;
import lombok.Data;

@Data
public class Land {

    private final Location islandLocation;
    private final Position landPosition;
    private final Vector minPoint, maxPoint;

    public Land(final Location origin, final Position landPosition, RegionInfo region) {
        this.islandLocation = landPosition.substract(origin)
                .setYaw(origin.getY())
                .setPitch(origin.getPitch());
        this.landPosition = landPosition;

        Vector regionOrigin = region.getOrigin();
        this.minPoint = region.getMinimumPoint().subtract(regionOrigin)
                .add(landPosition.getX(), landPosition.getY(), landPosition.getZ());
        this.maxPoint = region.getMaximumPoint().subtract(regionOrigin)
                .add(landPosition.getX(), landPosition.getY(), landPosition.getZ());
    }
}
