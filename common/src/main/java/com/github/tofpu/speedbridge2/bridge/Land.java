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
    private final Vector minPoint;
    private final Vector maxPoint;
    private final CuboidRegion cuboidRegion;

    public static Land newLand(int islandSlot, final Position landPosition, final RegionInfo region, final Location absolute) {
        System.out.println("Soon adapting both land position " + landPosition + " and absolute " + absolute);

        final Location islandLocation = resolveIslandLocation(landPosition, absolute);
        System.out.println("Merged into island location " + islandLocation);

        Vector regionOrigin = region.getOrigin();
        Vector minMax = resolveMinPoint(landPosition, region, regionOrigin);
        Vector maxPoint = resolveMaxPoint(landPosition, region, regionOrigin);

        return new Land(islandSlot, islandLocation, landPosition, minMax, maxPoint);
    }

    private static Vector resolveMaxPoint(Position landPosition, RegionInfo region, Vector regionOrigin) {
        return region.getMaximumPoint().subtract(regionOrigin)
                .add(landPosition.getX(), landPosition.getY(), landPosition.getZ());
    }

    private static Vector resolveMinPoint(Position landPosition, RegionInfo region, Vector regionOrigin) {
        return region.getMinimumPoint().subtract(regionOrigin)
                .add(landPosition.getX(), landPosition.getY(), landPosition.getZ());
    }

    /**
     * This method is used to determine the spot of the island.
     *
     * @param landPosition the position where the land will be placed
     * @param absolute the location offset
     * @return a location based on the land position with the offset
     */
    public static Location resolveIslandLocation(Position landPosition, Location absolute) {
        return landPosition.substract(absolute)
                .setYaw(absolute.getYaw())
                .setPitch(absolute.getPitch());
    }

    public static Land newLand(Land land, final Location absolute) {
        return new Land(land.islandSlot(), resolveIslandLocation(land.landPosition, absolute), land.landPosition, land.minPoint, land.maxPoint);
    }

    public Land(int islandSlot, Location islandLocation, Position landPosition, Vector minPoint, Vector maxPoint) {
        this.islandSlot = islandSlot;
        this.islandLocation = islandLocation;
        this.landPosition = landPosition;
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.cuboidRegion = new CuboidRegion(minPoint, maxPoint);
    }

    public boolean isInsideRegion(Vector vector) {
        return this.cuboidRegion.contains(vector);
    }

    public int islandSlot() {
        return islandSlot;
    }
}
