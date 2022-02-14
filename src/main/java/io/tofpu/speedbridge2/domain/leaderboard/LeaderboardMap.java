package io.tofpu.speedbridge2.domain.leaderboard;

import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class LeaderboardMap extends LinkedHashMap<Integer, BoardPlayer> {
    private final Map<BridgePlayer, Score> globalReference = new LinkedHashMap<>();
    private final Map<BridgePlayer, Score> tempBoardMap = new HashMap<>();

    public void load(final Map<Integer, BoardPlayer> boardPlayerMap) {
        for (final BoardPlayer boardPlayer : boardPlayerMap.values()) {
            final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(boardPlayer.getOwner());
            this.globalReference.put(bridgePlayer, boardPlayer.getScore());
        }
        updateLeaderboard();
    }

    public void append(final BridgePlayer bridgePlayer, final Score score) {
        final Score previousScore = globalReference.get(bridgePlayer);

        // if the player's previous score is higher than, or equal to the given score;
        // return
        if (previousScore != null && score.getScore() >= previousScore.getScore()) {
            return;
        }

        // insert the player's best score to the temp board
        this.tempBoardMap.put(bridgePlayer, score);
    }

    public void updateLeaderboard() {
        final AtomicInteger positionCounter = new AtomicInteger();

        final Map<BridgePlayer, Score> tempClone = new HashMap<>(tempBoardMap);
        tempClone.putAll(globalReference);

        final LinkedHashMap<Integer, BoardPlayer> sortedMap = tempClone.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .collect(Collectors.toMap(o -> {
                    return positionCounter.incrementAndGet();
                }, o -> {
                    final BridgePlayer key = o.getKey();
                    return new BoardPlayer(key.getName(), positionCounter.get(), key.getPlayerUid(), o.getValue());
                }, (o, o2) -> o, LinkedHashMap::new));

        final LinkedHashMap<Integer, BoardPlayer> globalBoardCopy = this;
        // combining the sorted map with the global leaderboard
        globalBoardCopy.putAll(sortedMap);

        // sorting the board clone by the key
        final Map<Integer, BoardPlayer> newBoardMap = globalBoardCopy.entrySet()
                .stream()
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
    }

    public Map<Integer, BoardPlayer> getGlobalBoardMap() {
        return this;
    }
}
