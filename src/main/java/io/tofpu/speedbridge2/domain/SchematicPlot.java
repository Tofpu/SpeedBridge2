package io.tofpu.speedbridge2.domain;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import org.bukkit.World;

public final class SchematicPlot {
    private final Island island;
    private final Clipboard schematicPlot;

    private GameIsland gameIsland;

    public SchematicPlot(final Island island) {
        this.island = island;
        this.schematicPlot = island.getIslandSchematic().getSchematicClipboard();
    }

    public void generatePlot(final World world, double[] positions) throws WorldEditException {
        // possibly make this operation async?
        final BukkitWorld bukkitWorld = new BukkitWorld(world);
        final WorldData worldData = bukkitWorld.getWorldData();

        final EditSession editSession = WorldEdit.getInstance()
                .getEditSessionFactory()
                .getEditSession((com.sk89q.worldedit.world.World) bukkitWorld, -1);

        final LocalSession session = WorldEdit.getInstance().getSession((Player) null);
        final ClipboardHolder clipboardHolder = session.getClipboard();
        final Operation operation = clipboardHolder
                .createPaste(editSession, worldData)
                .to(new BlockVector(positions[0], positions[1], positions[2]))
                .ignoreAirBlocks(true)
                .build();

        Operations.completeLegacy(operation);
    }

    public void reservePlot(final GameIsland gameIsland) {
        this.gameIsland = gameIsland;
    }

    public void freePlot() {
        this.gameIsland = null;
    }

    public boolean isPlotFree() {
        return gameIsland == null;
    }

    public int getSlot() {
        return this.island.getSlot();
    }

    public Clipboard getSchematicPlot() {
        return schematicPlot;
    }
}
