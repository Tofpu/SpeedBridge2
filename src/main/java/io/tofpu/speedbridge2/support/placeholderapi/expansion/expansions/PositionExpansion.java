package io.tofpu.speedbridge2.support.placeholderapi.expansion.expansions;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.IslandBoardPlayer;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.support.placeholderapi.expansion.AbstractExpansion;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@AutoRegister
public final class PositionExpansion extends AbstractExpansion {
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
        final CompletableFuture<IslandBoardPlayer.IslandBoard> retrieve = Leaderboard.INSTANCE.retrieve(bridgePlayer.getPlayerUid(), Integer.parseInt(args[1]));
        if (!retrieve.isDone()) {
            return "";
        }

        try {
            return retrieve.get()
                           .getPosition() + "";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getGlobalPosition(final BridgePlayer bridgePlayer) {
        final CompletableFuture<BoardPlayer> boardRetrieve = Leaderboard.INSTANCE.retrieve(bridgePlayer.getPlayerUid());
        // if the retrieve process is not immediate, return empty
        if (!boardRetrieve.isDone()) {
            return "";
        }

        try {
            return boardRetrieve.get()
                           .getPosition() + "";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }
}
