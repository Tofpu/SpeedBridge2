package io.tofpu.speedbridge2.domain.island.plot;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.GameIsland;
import org.bukkit.Bukkit;
import org.bukkit.World;

public final class IslandPlot {
    private final Island island;
    private final Clipboard schematicPlot;

    private final World world;
    private final double x;
    private final double y;
    private final double z;

    private final Vector minPoint;
    private final Vector maxPoint;

    private final PlotState plotState;

    public IslandPlot(final Island island, final World world, double[] positions) {
        this.island = island;
        this.schematicPlot = island.getSchematicClipboard();
        this.world = world;
        this.x = positions[0];
        this.y = positions[1];
        this.z = positions[2];

        this.minPoint = schematicPlot.getRegion()
                .getMinimumPoint()
                .subtract(schematicPlot.getOrigin())
                .add(new Vector(x, y, z));

        this.maxPoint = schematicPlot.getRegion()
                .getMaximumPoint()
                .subtract(schematicPlot.getOrigin())
                .add(new Vector(x, y, z));

        this.plotState = new PlotState();
    }

    public void generatePlot() throws WorldEditException {
        // possibly make this operation async?
        Bukkit.getLogger().info("generating plot!");

        final BukkitWorld bukkitWorld = new BukkitWorld(world);
        final WorldData worldData = bukkitWorld.getWorldData();

        final EditSession editSession = WorldEdit.getInstance()
                .getEditSessionFactory()
                .getEditSession((com.sk89q.worldedit.world.World) bukkitWorld, -1);

        final ClipboardHolder clipboardHolder = new ClipboardHolder(island.getSchematicClipboard(), worldData);
        final Operation operation = clipboardHolder.createPaste(editSession, worldData)
                .to(new BlockVector(x, y, z))
                .ignoreAirBlocks(true)
                .build();

        Operations.completeLegacy(operation);
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

    public Vector getMaxPoint() {
        return maxPoint;
    }

    public Vector getMinPoint() {
        return minPoint;
    }

    public Region region() {
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

    public Clipboard getSchematicPlot() {
        return schematicPlot;
    }
}
