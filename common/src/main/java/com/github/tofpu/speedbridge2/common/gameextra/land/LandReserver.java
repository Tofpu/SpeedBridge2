package com.github.tofpu.speedbridge2.common.gameextra.land;

import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.IslandSchematic;

public interface LandReserver {
    Land reserveLand(final IslandSchematic islandSchematic);

    boolean hasAvailableLand(int slot);
    void releaseLand(final Land land);

    default void clearLand(final Land land) {
        clearLand(land, false);
    }
    void clearLand(final Land land, final boolean shouldReleaseLand);
}
