package io.tofpu.speedbridge2.domain.schematic;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class SchematicPlot {
    private final Island island;
    private final Clipboard schematicPlot;

    private GameIsland gameIsland;

    public SchematicPlot(final Island island) {
        this.island = island;
        this.schematicPlot = island.getSchematicClipboard();
    }

    public void generatePlot(final World world, double[] positions) throws WorldEditException {
        // possibly make this operation async?
        Bukkit.getLogger().info("generating plot!");

        final BukkitWorld bukkitWorld = new BukkitWorld(world);
        final WorldData worldData = bukkitWorld.getWorldData();

        final EditSession editSession = WorldEdit.getInstance()
                .getEditSessionFactory()
                .getEditSession((com.sk89q.worldedit.world.World) bukkitWorld, -1);

        final ClipboardHolder clipboardHolder = new ClipboardHolder(island.getSchematicClipboard(), worldData);
        final Operation operation = clipboardHolder.createPaste(editSession, worldData)
                .to(new BlockVector(positions[0], positions[1], positions[2]))
                .ignoreAirBlocks(true)
                .build();

        Operations.completeLegacy(operation);

        Bukkit.getLogger().info("teleporting player now!");
        gameIsland.getGamePlayer().getPlayer().teleport(new Location(world, positions[0], positions[1], positions[2]));
    }

    public void reservePlot(final GameIsland gameIsland) {
        this.gameIsland = gameIsland;
//        gameIsland.getGamePlayer().getPlayer().teleport(new Location(world, positions[0], positions[1], positions[2]))
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
