package io.tofpu.speedbridge2.model.island.plot;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.EditSessionWrapper;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.multiworldedit.RegionWrapper;
import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.model.support.worldedit.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

public final class IslandPlot {
    private final Island island;

    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final double yaw;
    private final double pitch;

    private final Location location;

    private final VectorWrapper minPoint;
    private final VectorWrapper maxPoint;

    private final PlotState plotState;

    public IslandPlot(final Island island, final World world, double[] positions) {
        this.island = island;
        this.world = world;
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

        this.plotState = new PlotState();
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

    public void reservePlot(final GameIsland gameIsland) {
        this.plotState.reservePlotWith(gameIsland);
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

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
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
        return plotState.getGameIsland();
    }

    public void freePlot() {
        plotState.freePlot();
    }

    public boolean isPlotFree() {
        return plotState.isPlotFree();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IslandPlot{");
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
        sb.append(", plotState=")
                .append(plotState);
        sb.append('}');
        return sb.toString();
    }
}