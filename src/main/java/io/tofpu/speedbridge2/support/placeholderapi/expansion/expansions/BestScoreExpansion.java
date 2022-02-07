package io.tofpu.speedbridge2.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.support.placeholderapi.expansion.AbstractExpansion;

@AutoRegister
public final class BestScoreExpansion extends AbstractExpansion {
    @Override
    public String getIdentifier() {
        return "best_score";
    }

    @Override
    public String getDefaultAction(final BridgePlayer bridgePlayer) {
        return BridgeUtil.translateMiniMessageLegacy(Message.INSTANCE.EMPTY_SCORE_FORMAT);
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
            return "";
        }

        return BridgeUtil.formatNumber(bestScore.getScore());
    }
}
