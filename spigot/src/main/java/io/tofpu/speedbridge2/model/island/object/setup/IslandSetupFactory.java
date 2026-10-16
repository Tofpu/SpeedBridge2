package io.tofpu.speedbridge2.model.island.object.setup;

import com.google.common.base.Preconditions;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import com.tofpu.toolbar.api.toolbar.Toolbar;

public final class IslandSetupFactory {
    private static IslandService islandService;

    public static void init(final IslandService islandService) {
        IslandSetupFactory.islandService = islandService;
    }

    public static IslandSetup create(final IslandSetupFactoryType type,
                                     final Toolbar toolbar,
                                     final BridgePlayer player
            , final Island island, final IslandLand islandLand) {
        Preconditions.checkNotNull(islandService, "IslandService is not initialized");
        Preconditions.checkNotNull(toolbar, "toolbar cannot be null");
        Preconditions.checkNotNull(player, "BridgePlayer cannot be null");
        Preconditions.checkNotNull(island, "Island cannot be null");
        Preconditions.checkNotNull(islandLand, "IslandLand cannot be null");

        switch (type) {
            case BUILD:
                return new IslandBuildSetup(toolbar, player, island, islandLand, islandService);
            case SETUP:
                return new IslandSetup(toolbar, player, island, islandLand);
            default:
                throw new IllegalArgumentException("Unknown IslandSetupFactoryType: " + type);
        }
    }

    public enum IslandSetupFactoryType {
        BUILD,
        SETUP;
    }
}
