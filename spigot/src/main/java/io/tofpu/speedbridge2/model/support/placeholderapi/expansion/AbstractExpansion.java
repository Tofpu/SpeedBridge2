package io.tofpu.speedbridge2.model.support.placeholderapi.expansion;

import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;

public abstract class AbstractExpansion {
    public AbstractExpansion() {
        ExpansionHandler.INSTANCE.register(this);
    }

    public abstract String getIdentifier();

    public abstract String getDefaultAction(final BridgePlayer bridgePlayer);

    public abstract boolean passedRequirement(final BridgePlayer bridgePlayer, final String[] args);

    public abstract String runAction(final BridgePlayer bridgePlayer,
                                     final GamePlayer gamePlayer, final String[] args);
}
