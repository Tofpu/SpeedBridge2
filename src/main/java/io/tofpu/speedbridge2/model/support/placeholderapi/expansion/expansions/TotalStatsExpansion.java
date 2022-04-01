package io.tofpu.speedbridge2.model.support.placeholderapi.expansion.expansions;

import io.tofpu.speedbridge2.model.player.misc.stat.PlayerStatType;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.model.support.placeholderapi.expansion.AbstractExpansion;

public final class TotalStatsExpansion extends AbstractExpansion {
    private final String identifier;

    public TotalStatsExpansion(final String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDefaultAction(final BridgePlayer bridgePlayer) {
        return "";
    }

    @Override
    public boolean passedRequirement(final BridgePlayer bridgePlayer, final String[] args) {
        return true;
    }

    @Override
    public String runAction(final BridgePlayer bridgePlayer, final GamePlayer gamePlayer, final String[] args) {
        final PlayerStatType playerStatType = PlayerStatType.match(String.join("_", args));
        // this shouldn't be null
        if (playerStatType != null) {
            return bridgePlayer.findStatBy(playerStatType)
                    .getValue();
        }
        return "";
    }
}
