package io.tofpu.speedbridge2.model.island.object.setup;

import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.umbrella.domain.Umbrella;

public final class IslandBuildSetup extends IslandSetup {
    private final IslandService islandService;

    public IslandBuildSetup(final Umbrella umbrella, final BridgePlayer player, final Island island, final IslandLand islandLand, final IslandService islandService) {
        super(umbrella, player, island, islandLand);
        this.islandService = islandService;
    }

    @Override
    public boolean finish() {
        final boolean result = super.finish();
        if (!result) {
            return false;
        }

        islandService.registerIsland(getIsland());
        BridgeUtil.sendMessage(getPlayer(),
                String.format(Message.INSTANCE.islandHasBeenCreatedSchematic,
                        getIsland().getSlot(), getIsland().getSchematicName()));

        return true;
    }
}
