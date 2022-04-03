package io.tofpu.speedbridge2.model.island;

import com.google.common.base.Preconditions;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.IslandBuild;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import org.bukkit.Location;

public final class IslandFactory {
    private static IslandService islandService;
    private static ArenaManager arenaManager;

    public static void init(final IslandService islandService, final ArenaManager arenaManager) {
        IslandFactory.islandService = islandService;
        IslandFactory.arenaManager = arenaManager;
    }

    public static Island create(final int slot, final String category, final String schematic, final Location absoluteLocation) {
        return new Island(islandService, arenaManager, slot, category, schematic, absoluteLocation);
    }

    public static Island create(final IslandFactoryType type, final int slot, final String category) {
        Preconditions.checkNotNull(arenaManager, "ArenaManager is not initialized");
        Preconditions.checkNotNull(category, "Category cannot be null");

        switch (type) {
            case REGULAR:
                return new Island(islandService, arenaManager, slot, category);
            case BUILD:
                return new IslandBuild(islandService, arenaManager, slot, category);
            default:
                throw new IllegalArgumentException("Unknown IslandFactoryType: " + type);
        }
    }

    public static GameIsland createGame(final Island island, final GamePlayer gamePlayer) {
        Preconditions.checkNotNull(arenaManager, "ArenaManager is not initialized");
        Preconditions.checkNotNull(island, "Island cannot be null");
        Preconditions.checkNotNull(gamePlayer, "GamePlayer cannot be null");

        return new GameIsland(arenaManager, island, gamePlayer);
    }

    public enum IslandFactoryType {
        REGULAR, BUILD;
    }
}
