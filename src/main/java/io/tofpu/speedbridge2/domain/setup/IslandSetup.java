package io.tofpu.speedbridge2.domain.setup;

import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.multiworldedit.WorldEditAPI;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import org.bukkit.Location;
import org.bukkit.Material;

public final class IslandSetup {
    private final Island island;
    private final IslandPlot islandPlot;
    private Location playerSpawnPoint;

    public IslandSetup(final Island island, final IslandPlot islandPlot) {
        this.island = island;
        this.islandPlot = islandPlot;
    }

    public void setPlayerSpawnPoint(final Location playerSpawnPoint) {
        this.playerSpawnPoint = playerSpawnPoint;
    }

    public boolean finish() {
        if (!isReady()) {
            return false;
        }

        resetPlot();

        IslandSetupManager.INSTANCE.invalidate(this);
        return true;
    }

    public boolean isReady() {
        return playerSpawnPoint != null;
    }

    private void resetPlot() {
        final ClipboardWrapper clipboardWrapper = WorldEditAPI.getWorldEdit()
                .create(island.getSchematicClipboard());

        final VectorWrapper minimumPoint = clipboardWrapper.getMinimumPoint();
        final VectorWrapper maximumPoint = clipboardWrapper.getMaximumPoint();

        final int offset = 3;

        final int plotX = (int) islandPlot.getX() - offset;
        final int plotY = (int) islandPlot.getY() - offset;
        final int plotZ = (int) islandPlot.getZ() - offset;

        // resetting the blocks
        for (int x = 0; x < maximumPoint.getX() - minimumPoint.getX() + offset; x++) {
            for (int y = 0; y < maximumPoint.getY() - minimumPoint.getY() + offset; y++) {
                for (int z = 0;
                     z < maximumPoint.getZ() - minimumPoint.getZ() + offset; z++) {
                    final int blockX = plotX + x;
                    final int blockY = plotY + y;
                    final int blockZ = plotZ + z;

                    System.out.println(
                            "Removing block at " + blockX + " " + blockY + " " + blockZ);

                    this.islandPlot.getWorld()
                            .getBlockAt(blockX, blockY, blockZ)
                            .setType(Material.AIR);
                }
            }
        }
    }

    public Island getIsland() {
        return island;
    }

    public IslandPlot getIslandPlot() {
        return islandPlot;
    }
}
