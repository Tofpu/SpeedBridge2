package io.tofpu.speedbridge2.model.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.model.support.placeholderapi.expansion.AbstractExpansion;

@AutoRegister
public final class IslandSlotExpansion extends AbstractExpansion {
    @Override
    public String getIdentifier() {
        return "island_slot";
    }

    @Override
    public String getDefaultAction(final BridgePlayer bridgePlayer) {
        return "";
    }

    @Override
    public boolean passedRequirement(final BridgePlayer bridgePlayer, final String[] args) {
        return args.length == 2;
    }

    @Override
    public String runAction(final BridgePlayer bridgePlayer,
            final GamePlayer gamePlayer, final String[] args) {
        final GameIsland currentGame = bridgePlayer.getCurrentGame();
        if (currentGame == null) {
            return "";
        }
        return currentGame.getIsland().getSlot() + "";
    }
}
