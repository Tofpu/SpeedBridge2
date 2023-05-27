package io.tofpu.speedbridge2.model.island.arena;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;
import io.tofpu.speedbridge2.model.support.worldedit.Vector;
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

public final class ArenaManager {
    private final @NotNull Map<Integer, Collection<IslandLand>> ISLAND_PLOTS =
            new HashMap<>();
    private final AtomicInteger COUNTER = new AtomicInteger(100);

    private File worldDirectory;
    private World world;

    public void load() {
        this.world = Bukkit.createWorld(WorldCreator.name("speedbridge2")
                .generator(new EmptyChunkGenerator()));
        this.worldDirectory = new File( "speedbridge2");

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

    public @Nullable IslandLand reservePlot(final GameIsland gameIsland) {
        if (world == null) {
            Bukkit.getLogger()
                    .severe("The SpeedBridge2 world cannot be found! cancelled player's request to reserve a plot.");
            return null;
        }

        final Island island = gameIsland.getIsland();

        // return the available plot
        return getPlot(island, gameIsland);
    }

    private IslandLand getPlot(final Island island, final GameIsland gameIsland) {
        final int islandSlot = island.getSlot();

        // retrieving a collection of plots that is associated with the given island slot
        final Collection<IslandLand> islandLands = retrieve(islandSlot);

        // attempt to get an available plot with the given island slot
        IslandLand islandLand = getAvailablePlot(islandLands, islandSlot);

        // if we found an available plot, start reserving the plot
        if (islandLand != null) {
            islandLand.reserveWith(gameIsland);
        } else {
            // otherwise, create our own island plot
            islandLand = createIslandPlot(islandLands, island, gameIsland);
        }
        return islandLand;
    }

    private IslandLand getAvailablePlot(final Collection<IslandLand> islandLands,
            final int slot) {
        for (final IslandLand islandLand : islandLands) {
            // if it's not the same island plot, or the plot is not free; continue
            if (islandLand.getIsland().getSlot() != slot || !islandLand.isFree()) {
                continue;
            }

            BridgeUtil.debug("SchematicManager#getAvailablePlot: Found a free plot for " + slot + " slot!");
            return islandLand;
        }
        return null;
    }

    private IslandLand createIslandPlot(final Collection<IslandLand> islandLandList
            , final Island target, final GameIsland gameIsland) {
        BridgeUtil.debug("SchematicManager#createIslandPlot: Creating a new island plot for " + target.getSlot() + " slot!");

        final double[] positions = {COUNTER.get(), 100, 100};

        positions[0] += Math.abs(positions[0] - new IslandLand(target, world, positions).region().getMinimumPoint().getX());

        BridgeUtil.debug("=== island " + target.getSlot() + " ===");
        BridgeUtil.debug("Placing schematic at: " + Arrays.toString(positions));

        final IslandLand islandLand = new IslandLand(target, world, positions);
        BridgeUtil.debug("Island width is: " + islandLand.getWidth());

        COUNTER.getAndAdd(islandLand.getWidth() + ConfigurationManager.INSTANCE.getGeneralCategory().getIslandSpaceGap());

        BridgeUtil.debug("minimumPoint=" + serializeVector(islandLand.region().getMinimumPoint()));
        BridgeUtil.debug("maximumPoint=" + serializeVector(islandLand.region().getMaximumPoint()));
        BridgeUtil.debug("==========");

        // reserving the plot to player
        islandLand.reserveWith(gameIsland);
        try {
            // attempt to generate the plot
            islandLand.generatePlot();
        } catch (WorldEditException e) {
            throw new IllegalStateException(e);
        }

        // adding the plot for usability
        islandLandList.add(islandLand);

        // adding the new island plot to the schematic plot map
        ISLAND_PLOTS.put(target.getSlot(), islandLandList);
        return islandLand;
    }

    @NotNull
    private static String serializeVector(Vector vector) {
        return String.format("%s, %s, %s", vector.getX(), vector.getY(), vector.getZ());
    }

    public void resetWorld() {
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

    public Collection<IslandLand> retrieve(final int slot) {
        return ISLAND_PLOTS.getOrDefault(slot, new ArrayList<>());
    }

    public void clearPlot(final int slot) {
        ISLAND_PLOTS.remove(slot);
    }

    public File getWorldDirectory() {
        return worldDirectory;
    }

    public void unloadWorld() {
        if (world == null) {
            return;
        }

        movePlayersFromWorld();

        Bukkit.unloadWorld(world, false);
    }

    private void movePlayersFromWorld() {
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

    public World getWorld() {
        return this.world;
    }

    private static final class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public @NotNull ChunkData generateChunkData(final World world, final Random random, final int x, final int z, final BiomeGrid biome) {
            return createChunkData(world);
        }
    }
}
