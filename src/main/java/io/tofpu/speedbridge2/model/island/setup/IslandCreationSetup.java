package io.tofpu.speedbridge2.model.island.setup;

import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.plot.IslandPlot;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.umbrella.domain.Umbrella;

public final class IslandCreationSetup extends IslandSetup {
    public IslandCreationSetup(final Umbrella umbrella, final BridgePlayer player, final Island island, final IslandPlot islandPlot) {
        super(umbrella, player, island, islandPlot);
    }

    @Override
    public boolean finish() {
        final boolean result = super.finish();
        if (!result) {
            return false;
        }

        IslandService.INSTANCE.registerIsland(getIsland());
        BridgeUtil.sendMessage(getPlayer(),
                String.format(Message.INSTANCE.islandHasBeenCreatedSchematic,
                        getIsland().getSlot(), getIsland().getSchematicName()));

        return true;
    }
}
