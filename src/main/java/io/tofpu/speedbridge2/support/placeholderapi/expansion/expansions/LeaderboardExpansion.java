package io.tofpu.speedbridge2.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.extra.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.support.placeholderapi.expansion.AbstractExpansion;

@AutoRegister
public final class LeaderboardExpansion extends AbstractExpansion {
  @Override
  public String getIdentifier() {
    return "leaderboard";
  }

  @Override
  public String getDefaultAction(final BridgePlayer bridgePlayer) {
    return "";
  }

  @Override
  public boolean passedRequirement(final BridgePlayer bridgePlayer, final String[] args) {
    // this placeholder requires four arguments
    return args.length == 3;
  }

  @Override
  public String runAction(
      final BridgePlayer bridgePlayer, final GamePlayer gamePlayer, final String[] args) {
    final int position = Integer.parseInt(args[2]);

    final BoardPlayer boardPlayer;
    if (args[1].equalsIgnoreCase("global")) {
      boardPlayer =
          Leaderboard.INSTANCE.retrieve(Leaderboard.LeaderboardRetrieveType.GLOBAL, position);
    } else if (args[1].equalsIgnoreCase("session")) {
      boardPlayer =
          Leaderboard.INSTANCE.retrieve(Leaderboard.LeaderboardRetrieveType.SESSION, position);

      // if board player is null, return the empty session leaderboard message
      if (boardPlayer == null) {
        return BridgeUtil.translateMiniMessageLegacy(Message.INSTANCE.emptySessionLeaderboard);
      }
    } else {
      final Island island = IslandService.INSTANCE.findIslandBy(Integer.parseInt(args[1]));

      if (island == null) {
        return "";
      }

      boardPlayer = island.retrieveBy(position);
    }

    if (boardPlayer == null) {
      return "";
    }

    final Score bestScore = boardPlayer.getScore();
    if (bestScore == null) {
      return "";
    }

    return BridgeUtil.translate(
        ConfigurationManager.INSTANCE
            .getLeaderboardCategory()
            .getLeaderboardFormat()
            .replace("%position%", boardPlayer.getPosition() + "")
            .replace("%name%", boardPlayer.getName())
            .replace("%score%", BridgeUtil.formatNumber(bestScore.getScore())));
  }
}
