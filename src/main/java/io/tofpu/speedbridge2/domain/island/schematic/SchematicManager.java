package io.tofpu.speedbridge2.domain.island.schematic;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class SchematicManager {
    private static final @NotNull Map<Integer, Collection<IslandPlot>> ISLAND_PLOTS =
            new HashMap<>();
    private static final AtomicInteger COUNTER = new AtomicInteger();

    private static File worldDirectory;
    private static @Nullable World world;

    public static void load() {
        World world = Bukkit.getWorld("speedbridge2");
        if (world == null) {
            world = Bukkit.createWorld(WorldCreator.name("speedbridge2")
                    .generator(new EmptyChunkGenerator()));
        }

        SchematicManager.world = world;

        protectWorld(world);
    }

    private static void protectWorld(final @NotNull World world) {
        world.setFullTime(1000);
        world.setWeatherDuration(0);
        world.setStorm(false);
        world.setThundering(false);
        world.setPVP(false);
        world.setAutoSave(false);
        world.setMonsterSpawnLimit(0);

        world.setGameRuleValue("doDaylightCycle", "false");
    }

    public static @Nullable IslandPlot reservePlot(final GameIsland gameIsland) {
        if (world == null) {
            Bukkit.getLogger()
                    .severe("The SpeedBridge2 world cannot be found! cancelled player's request to reserve a plot.");
            return null;
        }

        final Island island = gameIsland.getIsland();
        final IslandPlot availablePlot = getPlot(island, gameIsland);

        // execute the on join method on game island
        gameIsland.onJoin();

        // return the available plot
        return availablePlot;
    }

    private static IslandPlot getPlot(final Island island, final GameIsland gameIsland) {
        final int islandSlot = island.getSlot();

        // retrieving a collection of plots that is associated with the given island slot
        final Collection<IslandPlot> islandPlots = retrieve(islandSlot);

        // attempt to get an available plot with the given island slot
        IslandPlot islandPlot = getAvailablePlot(islandPlots, islandSlot);

        // if we found an available plot, start reserving the plot
        if (islandPlot != null) {
            islandPlot.reservePlot(gameIsland);
        } else {
            // otherwise, create our own island plot
            islandPlot = createIslandPlot(islandPlots, island, gameIsland);
        }
        return islandPlot;
    }

    private static IslandPlot getAvailablePlot(final Collection<IslandPlot> islandPlots,
            final int slot) {
        for (final IslandPlot islandPlot : islandPlots) {
            // if it's not the same island plot, or the plot is not free; continue
            if (islandPlot.getIsland().getSlot() != slot || !islandPlot.isPlotFree()) {
                continue;
            }

            BridgeUtil.debug("found an available plot!");
            return islandPlot;
        }
        return null;
    }

    private static IslandPlot createIslandPlot(final Collection<IslandPlot> islandPlots
            , final Island target, final GameIsland gameIsland) {
        BridgeUtil.debug("no free plot available, creating our own plot!");

        final double[] positions = {100 * (COUNTER.getAndIncrement() + 100), 100, 100};

        final IslandPlot islandPlot = new IslandPlot(target, world, positions);

        // reserving the plot to player
        islandPlot.reservePlot(gameIsland);
        try {
            // attempt to generate the plot
            islandPlot.generatePlot();
        } catch (WorldEditException e) {
            e.printStackTrace();
            return null;
        }

        // adding the plot for usability
        islandPlots.add(islandPlot);

        // adding the new island plot to the schematic plot map
        ISLAND_PLOTS.put(target.getSlot(), islandPlots);
        return islandPlot;
    }

    public static void resetWorld() {
        final File worldFile = getWorldDirectory();
        if (worldFile != null && worldFile.exists()) {
            try {
                // delete the world outright
                FileUtils.forceDelete(worldFile);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static Collection<IslandPlot> retrieve(final int slot) {
        return ISLAND_PLOTS.getOrDefault(slot, new ArrayList<>());
    }

    public static void clearPlot(final int slot) {
        ISLAND_PLOTS.remove(slot);
    }

    public static File getWorldDirectory() {
        if (worldDirectory == null || !worldDirectory.exists()) {
            worldDirectory = new File( "speedbridge2");
        }
        return worldDirectory;
    }

    public static void unloadWorld() {
        if (world == null) {
            return;
        }

        movePlayersFromIslandWorld();

        Bukkit.unloadWorld(world, false);
    }

    private static void movePlayersFromIslandWorld() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld()
                    .getName()
                    .equals(world.getName())) {
                continue;
            }
            Location lobbyLocation = ConfigurationManager.INSTANCE.getLobbyCategory()
                    .getLobbyLocation();

            if (lobbyLocation == null) {
                lobbyLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
            }

            player.teleport(lobbyLocation);
        }
    }

    public static World getWorld() {
        return world;
    }

    public static final class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public @NotNull ChunkData generateChunkData(final World world, final Random random, final int x, final int z, final BiomeGrid biome) {
            return createChunkData(world);
        }
    }
}
