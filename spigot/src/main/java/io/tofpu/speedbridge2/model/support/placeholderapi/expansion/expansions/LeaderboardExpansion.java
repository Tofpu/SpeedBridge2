package io.tofpu.speedbridge2.model.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.model.leaderboard.object.BoardPlayer;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.model.support.placeholderapi.expansion.AbstractExpansion;

@AutoRegister
public final class LeaderboardExpansion extends AbstractExpansion {
    private final IslandService islandService;
    private final Leaderboard leaderboard;

    public LeaderboardExpansion(final IslandService islandService, final Leaderboard leaderboard) {
        this.islandService = islandService;
        this.leaderboard = leaderboard;
    }

    @Override
    public String getIdentifier() {
        return "leaderboard";
    }

    @Override
    public String getDefaultAction(final BridgePlayer bridgePlayer) {
        return "";
    }

    @Override
    public boolean passedRequirement(final BridgePlayer bridgePlayer,
                                     final String[] args) {
        // this placeholder requires four arguments
        return args.length == 3;
    }

    @Override
    public String runAction(final BridgePlayer bridgePlayer,
                            final GamePlayer gamePlayer, final String[] args) {
        final int position = parse(args, 2);
        if (position == -1) {
            return "Invalid Placeholder";
        }

        final BoardPlayer boardPlayer;
        if (args[1].equalsIgnoreCase("global")) {
            boardPlayer = leaderboard.retrieve(Leaderboard.LeaderboardRetrieveType.GLOBAL,
                    position);
        } else if (args[1].equalsIgnoreCase("session")) {
            boardPlayer = leaderboard.retrieve(Leaderboard.LeaderboardRetrieveType.SESSION, position);

            // if board player is null, return the empty session leaderboard message
            if (boardPlayer == null) {
                return BridgeUtil.miniMessageToLegacy(Message.INSTANCE.emptySessionLeaderboard);
            }
        } else {
            final int slot = parse(args, 1);
            if (slot == -1) {
                return "Invalid Placeholder";
            }
            final Island island = islandService.findIslandBy(slot);

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

        return BridgeUtil.translate(ConfigurationManager.INSTANCE.getLeaderboardCategory()
                .getLeaderboardFormat()
                .replace("%position%", boardPlayer.getPosition() + "")
                .replace("%name%", boardPlayer.getName())
                .replace("%score%", BridgeUtil.formatNumber(bestScore.getScore())));
    }

    public int parse(final String[] args, final int index) {
        try {
            return Integer.parseInt(args[index]);
        } catch (final NumberFormatException exception) {
            return -1;
        }
    }
}
