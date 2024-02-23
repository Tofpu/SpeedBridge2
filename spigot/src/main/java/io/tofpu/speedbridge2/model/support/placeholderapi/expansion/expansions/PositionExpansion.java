package io.tofpu.speedbridge2.model.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.model.leaderboard.object.BoardPlayer;
import io.tofpu.speedbridge2.model.leaderboard.object.IslandBoardPlayer;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.model.support.placeholderapi.expansion.AbstractExpansion;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@AutoRegister
public final class PositionExpansion extends AbstractExpansion {
    private final Leaderboard leaderboard;

    public PositionExpansion(final Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public String getIdentifier() {
        return "position";
    }

    @Override
    public String getDefaultAction(final BridgePlayer bridgePlayer) {
        return "";
    }

    @Override
    public boolean passedRequirement(final BridgePlayer bridgePlayer, final String[] args) {
        // impossible to figure out with this placeholder
        return true;
    }

    @Override
    public String runAction(final BridgePlayer bridgePlayer,
            final GamePlayer gamePlayer, final String[] args) {
        if (args.length == 2) { // returns island-based position
            return getIslandPosition(bridgePlayer, args);
        }
        // return global-based position
        return getGlobalPosition(bridgePlayer);
    }

    public String getIslandPosition(final BridgePlayer bridgePlayer, final String[] args) {
        final CompletableFuture<IslandBoardPlayer.IslandBoard> retrieve =
                leaderboard.retrieve(bridgePlayer.getPlayerUid(), Integer.parseInt(args[1]));
        if (!retrieve.isDone()) {
            return "";
        }

        try {
            final IslandBoardPlayer.IslandBoard islandBoard = retrieve.get();
            if (islandBoard == null) {
                return "0";
            }
            return islandBoard.getPosition() + "";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getGlobalPosition(final BridgePlayer bridgePlayer) {
        final CompletableFuture<BoardPlayer> boardRetrieve =
                leaderboard.retrieve(bridgePlayer.getPlayerUid());
        // if the retrieve process is not immediate, return empty
        if (!boardRetrieve.isDone()) {
            return "";
        }

        try {
            final BoardPlayer boardPlayer = boardRetrieve.get();
            if (boardPlayer == null) {
                BridgeUtil.debug("getGlobalPosition(): boardPlayer == null");
                return "0";
            }
            return boardPlayer.getPosition() + "";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }
}
