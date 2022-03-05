package io.tofpu.speedbridge2.domain.island.setup;

import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.support.worldedit.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public final class IslandSetup {
    private final BridgePlayer playerEditor;
    private final Island island;
    private final IslandPlot islandPlot;
    private final CuboidRegion cuboidRegion;
    private Location playerSpawnPoint;

    private boolean removed = false;

    public IslandSetup(final BridgePlayer playerEditor, final Island island, final IslandPlot islandPlot) {
        this.playerEditor = playerEditor;
        this.island = island;
        this.islandPlot = islandPlot;
        this.cuboidRegion = islandPlot.region();
    }

    public void setPlayerSpawnPoint(final Location playerSpawnPoint) {
        this.playerSpawnPoint = playerSpawnPoint;
    }

    public boolean isLocationValid(final Location newLocation) {
        return cuboidRegion.contains(newLocation.toVector());
    }

    public boolean finish() {
        if (!isReady()) {
            return false;
        }
        this.removed = true;
        playerEditor.toggleSetup();

        final Location absoluteLocation = islandPlot.getIslandLocation()
                .subtract(this.playerSpawnPoint);
        island.setAbsoluteLocation(absoluteLocation);

        // teleporting the player to the lobby location
        playerEditor.getPlayer()
                .teleport(ConfigurationManager.INSTANCE.getLobbyCategory()
                        .getLobbyLocation());

        resetPlot();
        IslandSetupManager.INSTANCE.invalidate(this);
        return true;
    }

    public boolean isRemoved() {
        return removed;
    }

    private void resetPlot() {
        final ClipboardWrapper clipboardWrapper = MultiWorldEditAPI.getMultiWorldEdit()
                .create(island.getSchematicClipboard());

        final VectorWrapper minimumPoint = clipboardWrapper.getMinimumPoint();
        final VectorWrapper maximumPoint = clipboardWrapper.getMaximumPoint();

        final int offset = 2;

        final int plotX = (int) islandPlot.getX() - offset;
        final int plotY = (int) islandPlot.getY() - offset;
        final int plotZ = (int) islandPlot.getZ() - offset;

        // TODO: This is causing lag, needs to be fixed!
        // resetting the blocks
        for (int x = 0; x < maximumPoint.getX() - minimumPoint.getX() + offset; x++) {
            for (int y = 0; y < maximumPoint.getY() - minimumPoint.getY() + offset; y++) {
                for (int z = 0;
                     z < maximumPoint.getZ() - minimumPoint.getZ() + offset; z++) {
                    final int blockX = plotX + x;
                    final int blockY = plotY + y;
                    final int blockZ = plotZ + z;

                    this.islandPlot.getWorld()
                            .getBlockAt(blockX, blockY, blockZ)
                            .setType(Material.AIR);
                }
            }
        }
    }

    public boolean isReady() {
        return !isRemoved() && playerSpawnPoint != null;
    }

    public void cancel() {
        if (isRemoved()) {
            return;
        }
        this.removed = true;
        IslandSetupManager.INSTANCE.invalidate(this);

        playerEditor.toggleSetup();

        // teleporting the player to the lobby location
        playerEditor.getPlayer()
                .teleport(ConfigurationManager.INSTANCE.getLobbyCategory()
                        .getLobbyLocation());

        resetPlot();
    }

    public UUID getEditorUid() {
        return playerEditor.getPlayerUid();
    }

    public Island getIsland() {
        return island;
    }

    public IslandPlot getIslandPlot() {
        return islandPlot;
    }
}
