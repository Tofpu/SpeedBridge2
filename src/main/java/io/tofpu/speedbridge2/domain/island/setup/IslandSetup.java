package io.tofpu.speedbridge2.domain.island.setup;

import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.multiworldedit.WorldEditAPI;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public final class IslandSetup {
    private final BridgePlayer playerEditor;
    private final Island island;
    private final IslandPlot islandPlot;
    private Location playerSpawnPoint;

    public IslandSetup(final BridgePlayer playerEditor, final Island island, final IslandPlot islandPlot) {
        this.playerEditor = playerEditor;
        this.island = island;
        this.islandPlot = islandPlot;
    }

    public void setPlayerSpawnPoint(final Location playerSpawnPoint) {
        this.playerSpawnPoint = playerSpawnPoint;
    }

    public boolean isLocationValid(final Location playerSpawnPoint) {
        final Location absoluteLocation = playerSpawnPoint.subtract(islandPlot.getLocation());
        return absoluteLocation.getX() >= 0 && absoluteLocation.getY() >= 0 &&
               absoluteLocation.getZ() >= 0;
    }

    public boolean finish() {
        if (!isReady()) {
            return false;
        }

        final Location absoluteLocation = playerSpawnPoint.subtract(islandPlot.getLocation());
        island.setRelativePoint(absoluteLocation);

        // teleporting the player to the lobby location
        playerEditor.getPlayer()
                .teleport(ConfigurationManager.INSTANCE.getLobbyCategory()
                        .getLobbyLocation());

        resetPlot();
        IslandSetupManager.INSTANCE.invalidate(this);
        return true;
    }

    public void cancel() {
        // teleporting the player to the lobby location
        playerEditor.getPlayer()
                .teleport(ConfigurationManager.INSTANCE.getLobbyCategory()
                        .getLobbyLocation());

        resetPlot();
        IslandSetupManager.INSTANCE.invalidate(this);
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

    public BridgePlayer getPlayerEditor() {
        return playerEditor;
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
