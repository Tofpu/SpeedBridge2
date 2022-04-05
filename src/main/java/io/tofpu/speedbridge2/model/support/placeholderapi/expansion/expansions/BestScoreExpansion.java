package io.tofpu.speedbridge2.model.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.model.support.placeholderapi.expansion.AbstractExpansion;

@AutoRegister
public final class BestScoreExpansion extends AbstractExpansion {
    @Override
    public String getIdentifier() {
        return "best_score";
    }

    @Override
    public String getDefaultAction(final BridgePlayer bridgePlayer) {
        return BridgeUtil.miniMessageToLegacy(Message.INSTANCE.emptyScoreFormat);
    }

    @Override
    public boolean passedRequirement(final BridgePlayer bridgePlayer, final String[] args) {
        return args.length == 2;
    }

    @Override
    public String runAction(final BridgePlayer bridgePlayer, final GamePlayer gamePlayer, final String[] args) {
        Score bestScore = null;
        for (final Score score : bridgePlayer.getScores()) {
            if (bestScore != null && score.compareTo(bestScore) >= 1) {
                continue;
            }
            bestScore = score;
        }

        if (bestScore == null) {
            return "0";
        }

        return BridgeUtil.formatNumber(bestScore.getScore());
    }
}
