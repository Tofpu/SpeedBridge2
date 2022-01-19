package io.tofpu.speedbridge2.domain;

import io.tofpu.speedbridge2.database.Databases;
import io.tofpu.speedbridge2.domain.misc.Score;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BridgePlayer {
    private final UUID playerUid;
    private final Map<Integer, Score> scoreMap;

    public static BridgePlayer of (UUID playerUid) {
        return new BridgePlayer(playerUid);
    }

    private BridgePlayer(final UUID playerUid) {
        this.playerUid = playerUid;
        this.scoreMap = new HashMap<>();
    }

    public Score setScoreIfLower(final int islandSlot, final long score) {
        final Score currentScore = this.scoreMap.get(islandSlot);
        final Score newScore = Score.of(islandSlot, score);

        // if the current score is null, or new score is less than the current score,
        // insert into the scoreMap
        if (currentScore == null || newScore.compareTo(currentScore) < 0) {
            setNewScore(newScore);
            return newScore;
        }

        return newScore;
    }

    public Score setInternalNewScore(final Score score) {
        this.scoreMap.put(score.getScoredOn(), score);
        return score;
    }

    public Score setNewScore(final Score score) {
        // if our score map contains the island, update the score
        if (this.scoreMap.containsKey(score.getScoredOn())) {
            Databases.PLAYER_DATABASE.update(this.playerUid, score);
        } else {
            // otherwise, insert the score
            Databases.PLAYER_DATABASE.insert(this.playerUid, score);
        }

        this.scoreMap.put(score.getScoredOn(), score);
        return score;
    }

    public Score setNewScore(final int islandSlot, final long newScore) {
        final Score score = Score.of(islandSlot, newScore);
        return setNewScore(score);
    }

    public Score findScoreBy(final int islandSlot) {
        return scoreMap.get(islandSlot);
    }

    public UUID getPlayerUid() {
        return playerUid;
    }

    public Iterable<? extends Score> getScores() {
        return this.scoreMap.values();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BridgePlayer{");
        sb.append("playerUid=").append(playerUid);
        sb.append(", scoreMap=").append(scoreMap);
        sb.append('}');
        return sb.toString();
    }
}