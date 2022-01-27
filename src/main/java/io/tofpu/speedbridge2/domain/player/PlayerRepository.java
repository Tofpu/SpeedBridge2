package io.tofpu.speedbridge2.domain.player;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public final class PlayerRepository {
    public CompletableFuture<Map<UUID, BridgePlayer>> loadPlayers() {
        return runAsync(() -> {
            final List<BridgePlayer> bridgePlayers = new ArrayList<>();
            try {
                bridgePlayers.addAll(Databases.PLAYER_DATABASE.getStoredPlayers().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            System.out.println("loaded bridgePlayers:");
            System.out.println(bridgePlayers);

            final Map<UUID, BridgePlayer> playerMap = new HashMap<>();
            if (!bridgePlayers.isEmpty()) {
                for (final BridgePlayer bridgePlayer : bridgePlayers) {
                    playerMap.put(bridgePlayer.getPlayerUid(), bridgePlayer);
                }
            }

            return playerMap;
        });
    }
}
