package io.tofpu.speedbridge2.domain.extra.leaderboard;

import io.tofpu.speedbridge2.domain.common.PlayerNameCache;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class LeaderboardMap extends LinkedHashMap<Integer, BoardPlayer> {
    private final Map<UUID, Score> globalReference = new LinkedHashMap<>();
    private final Map<UUID, Score> tempBoardMap = new HashMap<>();

    private final List<UUID> removeUserList = new ArrayList<>();

    public void load(final Map<Integer, BoardPlayer> boardPlayerMap) {
        for (final BoardPlayer boardPlayer : boardPlayerMap.values()) {
            //            final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(boardPlayer.getOwner());
            this.globalReference.put(boardPlayer.getOwner(), boardPlayer.getScore());
        }
        updateLeaderboard();
    }

    public void updateLeaderboard() {
        final AtomicInteger positionCounter = new AtomicInteger();

        final Map<UUID, Score> tempClone = new HashMap<>(tempBoardMap);
        tempClone.putAll(globalReference);

        final LinkedHashMap<Integer, BoardPlayer> sortedMap = tempClone.entrySet()
                .stream()
                .filter(uuidScoreEntry -> !removeUserList.contains(uuidScoreEntry.getKey()))
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .collect(Collectors.toMap(o -> positionCounter.incrementAndGet(), o -> {
                    final UUID uuid = o.getKey();
                    return new BoardPlayer(PlayerNameCache.INSTANCE.getOrDefault(uuid), positionCounter.get(), uuid, o.getValue());
                }, (o, o2) -> o, LinkedHashMap::new));

        final LinkedHashMap<Integer, BoardPlayer> globalBoardCopy = this;
        // combining the sorted map with the global leaderboard
        globalBoardCopy.putAll(sortedMap);

        // sorting the board clone by the key
        final Map<Integer, BoardPlayer> newBoardMap = globalBoardCopy.entrySet()
                .stream()
                .filter(uuidScoreEntry -> !removeUserList.contains(uuidScoreEntry.getValue().getOwner()))
                .sorted(Map.Entry.comparingByKey())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // clearing the global map
        this.clear();
        // inserting all the new board
        this.putAll(newBoardMap);

        // clearing the global reference
        this.globalReference.clear();
        // inserting the new leaderboard references
        this.globalReference.putAll(tempClone);

        this.tempBoardMap.clear();

        // clearing the removed list
        this.removeUserList.clear();
    }

    public void append(final BridgePlayer bridgePlayer, final Score score) {
        final Score previousScore = globalReference.get(bridgePlayer.getPlayerUid());

        // if the player's previous score is higher than, or equal to the given score;
        // return
        if (previousScore != null && score.getScore() >= previousScore.getScore()) {
            return;
        }

        // insert the player's best score to the temp board
        this.tempBoardMap.put(bridgePlayer.getPlayerUid(), score);
    }

    public void reset(final UUID uuid) {
        removeUserList.add(uuid);
    }

    private int getPlayerPosition(final UUID key) {
        for (final Map.Entry<Integer, BoardPlayer> playerEntry : this.entrySet()) {
            if (playerEntry.getValue().getOwner() != key) {
                continue;
            }
            return playerEntry.getKey();
        }
        return -1;
    }
}
