package io.tofpu.speedbridge2.model.island.object.setup;

import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.IslandBuild;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;
import io.tofpu.speedbridge2.model.island.object.setup.umbrella.IslandSetupUmbrella;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.umbrella.domain.Umbrella;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class IslandSetupHandler {
    public static final IslandSetupHandler INSTANCE = new IslandSetupHandler();
    public static final int ISLAND_SETUP_GAP = 10;

    private final Umbrella umbrella;
    private final Map<UUID, IslandSetup> islandSetupMap = new HashMap<>();

    private final AtomicInteger xAxisCounter = new AtomicInteger(100);

    private IslandSetupHandler() {
        this.umbrella = new IslandSetupUmbrella().getUmbrella();
    }

    private World world;

    /**
     * This function is called when the plugin is loaded
     */
    public void load() {
        this.world = Bukkit.getWorld("speedbridge2");
    }

    public boolean initiate(final BridgePlayer player, final Island island) {
        if (!islandSetupMap.containsKey(player.getPlayerUid())) {
            create(player, island).start();
            return true;
        }
        return false;
    }

    /**
     * This function creates a new IslandSetup object and adds it to the islandSetupMap
     *
     * @param player The player who is creating the island.
     * @param target The Island that the player is setting up.
     * @return The IslandSetup object.
     */

    private IslandSetup create(final BridgePlayer player, final Island target) {
        final double[] positions = {xAxisCounter.get(), 100, 0};
        positions[0] += Math.abs(positions[0] - new IslandLand(target, world, positions).region().getMinimumPoint().getX());

        final IslandSetup islandSetup;

        final IslandLand islandLand = new IslandLand(target, world, positions);
        xAxisCounter.addAndGet(islandLand.getWidth() + ISLAND_SETUP_GAP);
        if (target instanceof IslandBuild) {
            islandSetup = IslandSetupFactory.create(IslandSetupFactory.IslandSetupFactoryType.BUILD, umbrella, player, target, islandLand);
        } else {
            islandSetup = IslandSetupFactory.create(IslandSetupFactory.IslandSetupFactoryType.SETUP, umbrella, player, target, islandLand);
        }

        islandSetupMap.put(player.getPlayerUid(), islandSetup);
        return islandSetup;
    }

    /**
     * Finds the IslandSetup object with the given UUID
     *
     * @param uuid The UUID of the setup you want to find.
     * @return The IslandSetup object that matches the UUID.
     */
    public IslandSetup findSetupBy(final UUID uuid) {
        return islandSetupMap.get(uuid);
    }

     /**
      * Remove the given island setup from the island setup map
      *
      * @param islandSetup The island setup that is being invalidated.
      */
     public void invalidate(final IslandSetup islandSetup) {
        islandSetupMap.remove(islandSetup.getPlayerUid());
    }

    /**
     * Remove the island setup from the island setup map and cancel it if it's not already
     * removed
     *
     * @param uuid The UUID of the island to invalidate.
     */
    public void invalidate(final UUID uuid) {
        final IslandSetup islandSetup = islandSetupMap.remove(uuid);
        if (islandSetup != null && !islandSetup.isRemoved()) {
            islandSetup.cancel();
        }
    }
}
