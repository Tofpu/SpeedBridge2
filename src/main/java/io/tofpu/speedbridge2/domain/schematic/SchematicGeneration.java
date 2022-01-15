package io.tofpu.speedbridge2.domain.schematic;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class SchematicGeneration {
    public static final SchematicGeneration INSTANCE = new SchematicGeneration();
    private static final List<SchematicPlot> SCHEMATIC_PLOTS = new ArrayList<>();

    private World world;
    private SchematicGeneration() {}

    public void load(final Plugin plugin) {
        World world = Bukkit.getWorld("speedbridge2");
        if (world == null) {
            world = Bukkit.createWorld(WorldCreator.name("speedbridge2")
                    .generator(new EmptyChunkGenerator()));
        }
        this.world = world;

        final File bridgeWorld = new File(plugin.getDataFolder().getParentFile(), "speedbridge2");
        // if the bridge world exists, delete it on exit
        if (bridgeWorld.exists()) {
            bridgeWorld.deleteOnExit();
        }
    }

    public boolean reservePlot(final GameIsland gameIsland) {
        if (world == null) {
            Bukkit.getLogger().severe("The SpeedBridge2 world cannot be found! cancelled player's request to reserve a plot.");
            return false;
        }
        final Island island = gameIsland.getIsland();
        SchematicPlot selectedPlot = null;

        for (final SchematicPlot schematicPlot : SCHEMATIC_PLOTS) {
            // if a free plot was found, select the plot and break the loop
            if (schematicPlot.isPlotFree()) {
                selectedPlot = schematicPlot;
                break;
            }
        }

        // if the plot was not found, create our own & add it to our plots collection
        if (selectedPlot == null) {
            final double[] positions = {100 * (SCHEMATIC_PLOTS.size() + 100), 100, 100};

            selectedPlot = new SchematicPlot(island);

            // reserving the plot to player
            selectedPlot.reservePlot(gameIsland);
            try {
                // attempt to generate the plot
                selectedPlot.generatePlot(world, positions);
            } catch (WorldEditException e) {
                e.printStackTrace();
                return false;
            }

            // adding the plot for future use
            SCHEMATIC_PLOTS.add(selectedPlot);
        } else {
            // reserving the plot to player
            selectedPlot.reservePlot(gameIsland);
        }

        final GamePlayer gamePlayer = gameIsland.getGamePlayer();

        // setting the player island slot
        gamePlayer.setIslandSlot(island.getSlot());
        // teleports the player to plot
        gamePlayer.teleport(selectedPlot);

        return true;
    }

    public boolean freePlot(final GameIsland gameIsland) {
        SchematicPlot selectedPlot = null;

        for (final SchematicPlot schematicPlot : SCHEMATIC_PLOTS) {
            // if a plot's island equals to island, select the plot and break the loop
            if (schematicPlot.getGameIsland().equals(gameIsland)) {
                selectedPlot = schematicPlot;
                break;
            }
        }

        if (selectedPlot == null) {
            return false;
        }

        // free the plot for other players
        selectedPlot.freePlot();

        return true;
    }

    public static final class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public ChunkData generateChunkData(final World world, final Random random, final int x, final int z, final BiomeGrid biome) {
            return createChunkData(world);
        }
    }
}
