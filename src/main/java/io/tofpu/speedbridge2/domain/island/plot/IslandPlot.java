package io.tofpu.speedbridge2.domain.island.plot;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import io.tofpu.multiworldedit.*;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.support.worldedit.CuboidRegion;
import org.bukkit.Bukkit;
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

        final MultiWorldEdit worldEdit = MultiWorldEditAPI.getMultiWorldEdit();
        final ClipboardWrapper schematicPlot = worldEdit.create(island.getSchematicClipboard());

        final RegionWrapper regionWrapper = worldEdit.create(schematicPlot.to()
                .getRegion());
        final VectorWrapper origin = schematicPlot.getOrigin();

        // TODO: setup this
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
        // possibly make this operation async?
        Bukkit.getLogger()
                .info("generating plot!");

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
        return absoluteLocation.add(this.location);
    }

    public Location getPlotLocation() {
        return this.location;
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
}
