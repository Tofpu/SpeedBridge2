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

    public Land(final Position landPosition, final RegionInfo region, final Location absolute) {
        System.out.println("Soon adapting both land position " + landPosition + " and absolute " + absolute);
        this.islandLocation = landPosition.substract(absolute)
                .setYaw(absolute.getYaw())
                .setPitch(absolute.getPitch());
        System.out.println("Merged into island location " + islandLocation);
        this.landPosition = landPosition;

        Vector regionOrigin = region.getOrigin();
        this.minPoint = region.getMinimumPoint().subtract(regionOrigin)
                .add(landPosition.getX(), landPosition.getY(), landPosition.getZ());
        this.maxPoint = region.getMaximumPoint().subtract(regionOrigin)
                .add(landPosition.getX(), landPosition.getY(), landPosition.getZ());
    }
}
