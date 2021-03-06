package io.tofpu.speedbridge2.model.island.object.setup;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.support.worldedit.CuboidRegion;
import io.tofpu.umbrella.domain.Umbrella;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public class IslandSetup {
    private final Umbrella umbrella;
    private final BridgePlayer player;
    private final Island island;
    private final IslandLand islandLand;
    private final CuboidRegion cuboidRegion;
    private Location playerSpawnPoint;

    private boolean removed = false;

    public IslandSetup(final Umbrella umbrella, final BridgePlayer player, final Island island, final IslandLand islandLand) {
        this.umbrella = umbrella;
        this.player = player;
        this.island = island;
        this.islandLand = islandLand;
        this.cuboidRegion = islandLand.region();
    }

    /**
     * This function starts the island setup process
     */
    public void start() {
        player.toggleSetup();
        player.getPlayer().setGameMode(GameMode.CREATIVE);

        final IslandLand islandLand = getIslandPlot();
        try {
            islandLand.generatePlot();
        } catch (WorldEditException e) {
            throw new IllegalStateException(e);
        }

        // teleporting the player to the setup location
        player.getPlayer()
                .teleport(islandLand.getPlotLocation());

        // activate the player umbrella
        umbrella.activate(player.getPlayer());
    }

    /**
     * This function sets the player spawn point to the given location
     *
     * @param playerSpawnPoint The location where the player will spawn.
     */
    public void setPlayerSpawnPoint(final Location playerSpawnPoint) {
        this.playerSpawnPoint = playerSpawnPoint;
    }

    /**
     * Check if the new location is within the cuboid region.
     *
     * @param newLocation The location to check.
     * @return A boolean value.
     */
    public boolean isLocationValid(final Location newLocation) {
        return cuboidRegion.contains(newLocation.toVector());
    }

    /**
     * This function is called when the player clicks the "Finish" button in the setup GUI
     *
     * @return A boolean value.
     */
    public boolean finish() {
        if (!isReady()) {
            return false;
        }
        this.removed = true;

        final Location absoluteLocation = islandLand.getPlotLocation()
                .subtract(this.playerSpawnPoint);
        absoluteLocation.setYaw(this.playerSpawnPoint.getYaw());
        absoluteLocation.setPitch(this.playerSpawnPoint.getPitch());

        island.setAbsoluteLocation(absoluteLocation);

        resetState();

        // teleporting the player to the lobby location
        player.getPlayer()
                .teleport(ConfigurationManager.INSTANCE.getLobbyCategory()
                        .getLobbyLocation());

        resetPlot();
        IslandSetupHandler.INSTANCE.invalidate(this);
        return true;
    }

    /**
     * Returns true if the node is removed from the tree
     *
     * @return The boolean value of the removed variable.
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * It resets the plot to air
     */
    private void resetPlot() {
        final ClipboardWrapper clipboardWrapper = MultiWorldEditAPI.getMultiWorldEdit()
                .create(island.getSchematicClipboard());

        final VectorWrapper minimumPoint = clipboardWrapper.getMinimumPoint();
        final VectorWrapper maximumPoint = clipboardWrapper.getMaximumPoint();

        final int offset = 2;

        final int plotX = (int) islandLand.getX() - offset;
        final int plotY = (int) islandLand.getY() - offset;
        final int plotZ = (int) islandLand.getZ() - offset;

        // resetting the blocks
        for (int x = 0; x < maximumPoint.getX() - minimumPoint.getX() + offset; x++) {
            for (int y = 0; y < maximumPoint.getY() - minimumPoint.getY() + offset; y++) {
                for (int z = 0;
                     z < maximumPoint.getZ() - minimumPoint.getZ() + offset; z++) {
                    final int blockX = plotX + x;
                    final int blockY = plotY + y;
                    final int blockZ = plotZ + z;

                    this.islandLand.getWorld()
                            .getBlockAt(blockX, blockY, blockZ)
                            .setType(Material.AIR);
                }
            }
        }
    }

    /**
     * Returns true if the player is not removed and has a player spawn point
     *
     * @return A boolean value.
    */
    public boolean isReady() {
        return !isRemoved() && playerSpawnPoint != null;
    }

    /**
     * This function is called when the player clicks the "Cancel" button. It removes the
     * island from the IslandSetupManager and teleports the player to the lobby location
     */
    public void cancel() {
        if (isRemoved()) {
            return;
        }
        this.removed = true;
        IslandSetupHandler.INSTANCE.invalidate(this);

        resetState();

        // teleporting the player to the lobby location
        player.getPlayer()
                .teleport(ConfigurationManager.INSTANCE.getLobbyCategory()
                        .getLobbyLocation());

        resetPlot();
    }

    private void resetState() {
        player.toggleSetup();

        // inactivate the setup umbrella
        umbrella.inactivate(player.getPlayer());

        // setting the player's gamemode back to survival
        player.getPlayer().setGameMode(GameMode.SURVIVAL);
    }

    /**
     * Returns the UUID of the player that is currently editing the island
     *
     * @return The UUID of the player that is currently editing the island.
    */
    public UUID getPlayerUid() {
        return player.getPlayerUid();
    }

    /**
     * @return The player that is currently editing the island.
     */
    public BridgePlayer getPlayer() {
        return player;
    }

    /**
     * Returns the island that the player is currently on
     *
     * @return The Island object that is being returned is the Island object that is being
     * passed in.
     */
    public Island getIsland() {
        return island;
    }

    /**
     * Returns the plot that the island is on
     *
     * @return The IslandPlot object that is associated with the current plot.
     */
    public IslandLand getIslandPlot() {
        return islandLand;
    }
}
