package io.tofpu.speedbridge2.domain.island.schematic;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.speedbridge2.domain.island.object.GameIsland;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class SchematicManager {
    public static final SchematicManager INSTANCE = new SchematicManager();
    private static final List<IslandPlot> SCHEMATIC_PLOTS = new ArrayList<>();

    private World world;
    private SchematicManager() {}

    public void load(final Plugin plugin) {
        World world = Bukkit.getWorld("speedbridge2");
        if (world == null) {
            world = Bukkit.createWorld(WorldCreator.name("speedbridge2")
                    .generator(new EmptyChunkGenerator()));
        }
        this.world = world;

        final File bridgeWorld = new File(plugin.getDataFolder()
                .getParentFile(), "speedbridge2");
        // if the bridge world exists, delete it on exit
        if (bridgeWorld.exists()) {
            bridgeWorld.deleteOnExit();
        }

        protectWorld(world);
    }

    private void protectWorld(final @NotNull World world) {
        world.setFullTime(1000);
        world.setWeatherDuration(0);
        world.setStorm(false);
        world.setThundering(false);
        world.setPVP(false);
        world.setAutoSave(false);
        world.setMonsterSpawnLimit(0);

        world.setGameRuleValue("doDaylightCycle", "false");
    }

    public IslandPlot reservePlot(final GameIsland gameIsland) {
        if (world == null) {
            Bukkit.getLogger()
                    .severe("The SpeedBridge2 world cannot be found! cancelled player's request to reserve a plot.");
            return null;
        }

        final Island island = gameIsland.getIsland();
        IslandPlot selectedPlot = null;

        for (final IslandPlot islandPlot : SCHEMATIC_PLOTS) {
            // if a free plot was found that equals to the same slot, select the plot and
            // break the loop
            if (islandPlot.getIsland().getSlot() == gameIsland.getIsland()
                    .getSlot() && islandPlot.isPlotFree()) {
                System.out.println("found a available plot!");
                selectedPlot = islandPlot;
                break;
            }
        }

        // if the plot was not found, create our own & add it to our plots collection
        if (selectedPlot == null) {
            System.out.println("no free plot available, creating our own plot!");

            final double[] positions = {100 * (SCHEMATIC_PLOTS.size() + 100), 100, 100};

            selectedPlot = new IslandPlot(island, world, positions);

            // reserving the plot to player
            selectedPlot.reservePlot(gameIsland);
            try {
                // attempt to generate the plot
                selectedPlot.generatePlot();
            } catch (WorldEditException e) {
                e.printStackTrace();
                return null;
            }

            // adding the plot for future use
            SCHEMATIC_PLOTS.add(selectedPlot);
        } else {
            // reserving the plot to player
            selectedPlot.reservePlot(gameIsland);
        }

        final GamePlayer gamePlayer = gameIsland.getGamePlayer();

        // setting the player island slot
        gamePlayer.setCurrentGame(gameIsland);
        // teleports the player to plot
        gamePlayer.teleport(selectedPlot);

        gameIsland.onJoin();

        return selectedPlot;
    }

    public boolean freePlot(final GameIsland gameIsland) {
        IslandPlot selectedPlot = null;

        for (final IslandPlot islandPlot : SCHEMATIC_PLOTS) {
            // if a plot's island equals to island, select the plot and break the loop
            final GameIsland island = islandPlot.getGameIsland();
            if (island != null && island.equals(gameIsland)) {
                System.out.println("found the identical island plot!");
                selectedPlot = islandPlot;
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
