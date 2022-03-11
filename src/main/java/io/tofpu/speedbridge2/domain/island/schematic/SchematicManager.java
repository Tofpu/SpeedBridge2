package io.tofpu.speedbridge2.domain.island.schematic;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SchematicManager {
  public static final @NotNull SchematicManager INSTANCE = new SchematicManager();

  private static final @NotNull Map<Integer, Collection<IslandPlot>> ISLAND_PLOTS = new HashMap<>();
  private static final AtomicInteger COUNTER = new AtomicInteger();

  private File worldFile;
  private @Nullable World world;

  private SchematicManager() {}

  public void load(final @NotNull Plugin plugin) {
    World world = Bukkit.getWorld("speedbridge2");
    if (world == null) {
      world =
          Bukkit.createWorld(
              WorldCreator.name("speedbridge2").generator(new EmptyChunkGenerator()));
    }
    this.world = world;

    worldFile = new File(plugin.getDataFolder().getParentFile(), "speedbridge2");

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

  public @Nullable IslandPlot reservePlot(final GameIsland gameIsland) {
    if (world == null) {
      Bukkit.getLogger()
          .severe(
              "The SpeedBridge2 world cannot be found! cancelled player's request to reserve a"
                  + " plot.");
      return null;
    }

    final Island island = gameIsland.getIsland();
    final int islandSlot = island.getSlot();
    final Collection<IslandPlot> islandPlots = retrieve(islandSlot);
    IslandPlot availablePlot = getAvailablePlot(islandPlots, islandSlot);

    // if we found an available plot, start reserving the plot
    if (availablePlot != null) {
      availablePlot.reservePlot(gameIsland);
    } else {
      // otherwise, create our own island plot
      availablePlot = createIslandPlot(islandPlots, island, gameIsland);
    }

    final GamePlayer gamePlayer = gameIsland.getGamePlayer();

    // setting the player island slot
    gamePlayer.setCurrentGame(gameIsland);
    // teleports the player to plot
    gamePlayer.teleport(availablePlot);

    gameIsland.onJoin();

    return availablePlot;
  }

  private IslandPlot getAvailablePlot(final Collection<IslandPlot> islandPlots, final int slot) {
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

  private IslandPlot createIslandPlot(
      final Collection<IslandPlot> islandPlots, final Island target, final GameIsland gameIsland) {
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

  public Collection<IslandPlot> retrieve(final int slot) {
    return ISLAND_PLOTS.getOrDefault(slot, new ArrayList<>());
  }

  public void clearPlot(final int slot) {
    ISLAND_PLOTS.remove(slot);
  }

  public File getWorldFile() {
    return worldFile;
  }

  public static final class EmptyChunkGenerator extends ChunkGenerator {
    @Override
    public @NotNull ChunkData generateChunkData(
        final World world, final Random random, final int x, final int z, final BiomeGrid biome) {
      return createChunkData(world);
    }
  }
}
