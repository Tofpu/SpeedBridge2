package io.tofpu.speedbridge2.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.support.placeholderapi.expansion.AbstractExpansion;

@AutoRegister
public final class TimerExpansion extends AbstractExpansion {
    @Override
    public String getIdentifier() {
        return "timer";
    }

    @Override
    public String getDefaultAction(final BridgePlayer bridgePlayer) {
        return "0";
    }

    @Override
    public boolean passedRequirement(final BridgePlayer bridgePlayer, final String[] args) {
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();

        return gamePlayer != null && gamePlayer.hasTimerStarted();
    }

    @Override
    public String runAction(final BridgePlayer bridgePlayer,
            final GamePlayer gamePlayer, final String[] args) {
        return BridgeUtil.formatNumber(BridgeUtil.nanoToSeconds(gamePlayer.getTimer()));
    }
}
