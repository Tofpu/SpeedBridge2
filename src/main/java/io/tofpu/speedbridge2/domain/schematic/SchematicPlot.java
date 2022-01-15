package io.tofpu.speedbridge2.domain.schematic;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public final class SchematicPlot {
    private final Island island;
    private final Clipboard schematicPlot;

    private GameIsland gameIsland;
    private World world;
    private double x;
    private double y;
    private double z;

    public SchematicPlot(final Island island) {
        this.island = island;
        this.schematicPlot = island.getSchematicClipboard();
    }

    public void generatePlot(final World world, double[] positions) throws WorldEditException {
        this.world = world;
        this.x = positions[0];
        this.y = positions[1];
        this.z = positions[2];

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

        final Clipboard clipboard = clipboardHolder.getClipboard();
        for (double x = clipboard.getMinimumPoint().getX(); x <= clipboard.getMaximumPoint().getX(); x++) {
            for (double y = clipboard.getMinimumPoint().getY(); y <= clipboard.getMaximumPoint().getY(); y++) {
                for (double z = clipboard.getMinimumPoint().getZ(); z <= clipboard.getMaximumPoint().getZ(); z++) {
                    final BaseBlock block = clipboard.getLazyBlock(new BlockVector(x, y, z));
                    // temporally
                    final Material material = Material.getMaterial(block.getId());
                    if (material == Material.AIR) {
                        continue;
                    }

                    Bukkit.broadcastMessage(material + " (" + x + ", " + y + ", " + z + ")");
                }
            }
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

    public GameIsland getGameIsland() {
        return gameIsland;
    }

    public Clipboard getSchematicPlot() {
        return schematicPlot;
    }
}
