package io.tofpu.speedbridge2.domain.setup;

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
        IslandSetup islandSetup = islandSetupMap.get(playerUid);

        if (islandSetup != null) {
            return false;
        }

        final double[] positions = {100 * (islandSetupMap.size() + 100), 100, 0};

        // TODO: create a custom wrapper of island plot
        islandSetup = new IslandSetup(playerUid, island, new IslandPlot(island, world, positions));
        islandSetupMap.put(playerUid, islandSetup);

        final IslandPlot islandPlot = islandSetup.getIslandPlot();
        try {
            islandPlot.generatePlot();
        } catch (WorldEditException e) {
            throw new IllegalStateException(e);
        }

        // teleporting the player to the setup location
        bridgePlayer.getPlayer()
                .teleport(islandPlot.getLocation());
        return true;
    }

    public IslandSetup findSetupBy(final UUID uuid) {
        return islandSetupMap.get(uuid);
    }

    public void invalidate(final IslandSetup islandSetup) {
        islandSetupMap.remove(islandSetup.getEditorUid());
    }

    public void invalidate(final UUID uuid) {
        islandSetupMap.remove(uuid);
    }
}
