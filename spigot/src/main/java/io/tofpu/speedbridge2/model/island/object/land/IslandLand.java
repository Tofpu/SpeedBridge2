package io.tofpu.speedbridge2.model.island.object.land;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import io.tofpu.multiworldedit.*;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.support.worldedit.CuboidRegion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

public class IslandLand {
    private final Island island;

    private final World world;
    private final double[] positions;
    private final double x;
    private final double y;
    private final double z;
    private final double yaw;
    private final double pitch;

    private final Location location;

    private final VectorWrapper minPoint;
    private final VectorWrapper maxPoint;

    private final LandState landState;

    public IslandLand(final Island island, final World world, double[] positions) {
        this.island = island;
        this.world = world;
        this.positions = positions;
        this.x = positions[0];
        this.y = positions[1];
        this.z = positions[2];

        final ClipboardWrapper schematicPlot = island.getSchematicClipboardWrapper();

        final RegionWrapper regionWrapper = schematicPlot.region();
        final VectorWrapper origin = schematicPlot.getOrigin();

        this.yaw = 0f;
        this.pitch = 0f;

        this.minPoint = regionWrapper.getMinimumPoint()
                .subtract(origin)
                .add(this.x, y, this.z);
        this.maxPoint = regionWrapper.getMaximumPoint()
                .subtract(origin)
                .add(this.x, y, this.z);

        this.location = new Location(world, this.x, y, this.z, (float) yaw, (float) pitch);
        this.landState = new LandState();
    }

    public void generatePlot() throws WorldEditException {
        // TODO: Make this generation operation async
        BridgeUtil.debug("Generating plot at " + this.location.toString() + " for " +
                "island " + this.island.getSlot());

        final BukkitWorld bukkitWorld = new BukkitWorld(world);

        try (final EditSessionWrapper editSessionWrapper = MultiWorldEditAPI.getMultiWorldEdit()
                .create(bukkitWorld, -1)) {
            final Clipboard schematicClipboard = getIsland().getSchematicClipboard();
            final EditSession editSession = editSessionWrapper.to();

            final Operation operation = MultiWorldEditAPI.getMultiWorldEdit()
                    .create(schematicClipboard, editSession, bukkitWorld)
                    .to((int) x, (int) y, (int) z)
                    .ignoreAirBlocks(true)
                    .build();

            Operations.completeLegacy(operation);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void reserveWith(final GameIsland gameIsland) {
        this.landState.reserveWith(gameIsland);
    }

    public World getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Location getIslandLocation() {
        final Location absoluteLocation = island.getAbsoluteLocation();

        // if the absolute location were not defined, return the island plot location
        if (absoluteLocation == null) {
            return location;
        }

        // otherwise, add plot's location to the absolute location
        final Location islandLocation = getPlotLocation().subtract(absoluteLocation);
        islandLocation.setYaw(absoluteLocation.getYaw());
        islandLocation.setPitch(absoluteLocation.getPitch());

        return islandLocation;
    }

    public Location getPlotLocation() {
        return this.location.clone();
    }

    public CuboidRegion region() {
        return new CuboidRegion(minPoint, maxPoint);
    }

    public Island getIsland() {
        return island;
    }

    public GameIsland getGameIsland() {
        return landState.getGame();
    }

    public void free() {
        landState.free();
    }

    public boolean isFree() {
        return landState.isFree();
    }

    public int getWidth() {
        return getIsland().getSchematicClipboard().getRegion().getWidth();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IslandLand{");
        sb.append("island=")
                .append(island);
        sb.append(", world=")
                .append(world);
        sb.append(", x=")
                .append(x);
        sb.append(", y=")
                .append(y);
        sb.append(", z=")
                .append(z);
        sb.append(", yaw=")
                .append(yaw);
        sb.append(", pitch=")
                .append(pitch);
        sb.append(", location=")
                .append(location);
        sb.append(", minPoint=")
                .append(minPoint);
        sb.append(", maxPoint=")
                .append(maxPoint);
        sb.append(", landState=")
                .append(landState);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof IslandLand)) {
            return false;
        }

        final IslandLand that = (IslandLand) o;

        return new EqualsBuilder().append(getIsland(), that.getIsland())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getIsland())
                .toHashCode();
    }

    public double[] getPositions() {
        return this.positions;
    }

    private static class LandState {
        private GameIsland gameIsland;

        public void reserveWith(final GameIsland gameIsland) {
            this.gameIsland = gameIsland;
        }

        public void free() {
            gameIsland = null;
        }

        public boolean isFree() {
            return gameIsland == null;
        }

        public GameIsland getGame() {
            return gameIsland;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("LandState{");
            sb.append("gameIsland=")
                    .append(gameIsland);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof LandState)) {
                return false;
            }

            final LandState landState = (LandState) o;

            return new EqualsBuilder().append(gameIsland, landState.gameIsland)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(gameIsland)
                    .toHashCode();
        }
    }
}
