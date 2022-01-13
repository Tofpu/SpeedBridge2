package io.tofpu.speedbridge2.domain;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
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
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(world))) {
            Operation operation = new ClipboardHolder(this.schematicPlot)
                    .createPaste(editSession)
                    .to(BlockVector3.at(positions[0], positions[1], positions[2]))
                    // configure here
                    .build();
            Operations.complete(operation);
        }
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
