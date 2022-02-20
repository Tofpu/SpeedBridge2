package io.tofpu.speedbridge2.domain.island.plot;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import io.tofpu.multiworldedit.EditSessionWrapper;
import io.tofpu.multiworldedit.WorldEditAPI;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.support.worldedit.CuboidRegion;
import io.tofpu.speedbridge2.support.worldedit.Vector;
import io.tofpu.speedbridge2.support.worldedit.util.WorldEditReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

public final class IslandPlot {
    private final Island island;

    private final World world;
    private final double x;
    private final double y;
    private final double z;

    private final Vector minPoint;
    private final Vector maxPoint;

    private final PlotState plotState;

    public IslandPlot(final Island island, final World world, double[] positions) {
        this.island = island;
        Clipboard schematicPlot = island.getSchematicClipboard();
        this.world = world;
        this.x = positions[0];
        this.y = positions[1];
        this.z = positions[2];

        final Vector origin = WorldEditReflectionUtil.getOriginFromRegion(schematicPlot);

        this.minPoint = WorldEditReflectionUtil.getMinimumPointFromRegion(schematicPlot.getRegion())
                .subtract(origin)
                .add(x, y, z);

        this.maxPoint = WorldEditReflectionUtil.getMaximumPointFromRegion(schematicPlot.getRegion())
                .subtract(origin)
                .add(x, y, z);

        this.plotState = new PlotState();
    }

    public void generatePlot() throws WorldEditException {
        // possibly make this operation async?
        Bukkit.getLogger()
                .info("generating plot!");

        final BukkitWorld bukkitWorld = new BukkitWorld(world);

        try (final EditSessionWrapper editSessionWrapper = WorldEditAPI.getWorldEdit()
                .create(bukkitWorld, -1)) {
            final Clipboard schematicClipboard = getIsland().getSchematicClipboard();
            final EditSession editSession = editSessionWrapper.get();

            final Operation operation = WorldEditAPI.getWorldEdit()
                    .create(schematicClipboard, editSession, bukkitWorld)
                    .to((int) x, (int) y, (int) z)
                    .ignoreAirBlocks(true)
                    .build();

            Operations.completeLegacy(operation);
        } catch (IOException e) {
            e.printStackTrace();
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
