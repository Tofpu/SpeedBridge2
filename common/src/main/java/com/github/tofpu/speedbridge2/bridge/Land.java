package com.github.tofpu.speedbridge2.bridge;

import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;
import lombok.Data;

@Data
public class Land {

    private final int islandSlot;
    private final Location islandLocation;
    private final Position landPosition;
    private final Vector minPoint, maxPoint;
    private final CuboidRegion cuboidRegion;

    public Land(int islandSlot, final Position landPosition, final RegionInfo region, final Location absolute) {
        this.islandSlot = islandSlot;
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

        this.cuboidRegion = new CuboidRegion(minPoint, maxPoint);
    }

    public boolean isInsideRegion(Vector vector) {
        return this.cuboidRegion.contains(vector);
    }

    public int islandSlot() {
        return islandSlot;
    }
}