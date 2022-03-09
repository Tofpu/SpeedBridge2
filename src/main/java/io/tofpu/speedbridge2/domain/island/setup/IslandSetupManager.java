package io.tofpu.speedbridge2.domain.island.setup;

import com.sk89q.worldedit.WorldEditException;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class IslandSetupManager {
    public static final IslandSetupManager INSTANCE = new IslandSetupManager();

    private final Map<UUID, IslandSetup> islandSetupMap = new HashMap<>();

    private World world;

    public void load() {
        this.world = Bukkit.getWorld("speedbridge2");
    }

    public boolean startSetup(final BridgePlayer bridgePlayer, final Island island) {
        final UUID playerUid = bridgePlayer.getPlayerUid();

        if (islandSetupMap.get(playerUid) != null) {
            return false;
        }
        bridgePlayer.toggleSetup();

        final IslandSetup islandSetup = create(bridgePlayer, island);
        final IslandPlot islandPlot = islandSetup.getIslandPlot();
        try {
            islandPlot.generatePlot();
        } catch (WorldEditException e) {
            throw new IllegalStateException(e);
        }

        // teleporting the player to the setup location
        bridgePlayer.getPlayer()
                .teleport(islandPlot.getPlotLocation());
        return true;
    }

    private IslandSetup create(final BridgePlayer player, final Island target) {
        final double[] positions = {100 * (islandSetupMap.size() + 100), 100, 0};
        final IslandSetup islandSetup = new IslandSetup(player, target, new IslandPlot(target, world, positions));

        islandSetupMap.put(player.getPlayerUid(), islandSetup);

        return islandSetup;
    }

    public IslandSetup findSetupBy(final UUID uuid) {
        return islandSetupMap.get(uuid);
    }

    public void invalidate(final IslandSetup islandSetup) {
        islandSetupMap.remove(islandSetup.getEditorUid());
    }

    public void invalidate(final UUID uuid) {
        final IslandSetup islandSetup = islandSetupMap.remove(uuid);
        if (islandSetup != null && !islandSetup.isRemoved()) {
            islandSetup.cancel();
        }
    }
}
