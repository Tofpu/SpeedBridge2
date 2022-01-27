package io.tofpu.speedbridge2.domain.player.object;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.player.misc.Score;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BridgePlayer extends CommonBridgePlayer<Player> {
    private final UUID playerUid;
    private final Map<Integer, Score> scoreMap;

    private final Player player;
    private GamePlayer gamePlayer;

    public static BridgePlayer of(final BridgePlayer copy) {
        return new BridgePlayer(copy);
    }

    public static BridgePlayer of(final UUID playerUid) {
        return new BridgePlayer(playerUid);
    }

    private BridgePlayer(final BridgePlayer copy) {
        this(copy.playerUid);
        this.scoreMap.putAll(copy.scoreMap);
    }

    private BridgePlayer(final UUID playerUid) {
        this.playerUid = playerUid;
        this.scoreMap = new HashMap<>();

        if (playerUid != null) {
            this.player = Bukkit.getPlayer(playerUid);
        } else {
            this.player = null;
        }
    }

    public Score setScoreIfLower(final int islandSlot, final double score) {
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

    public void setGamePlayer(final GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public boolean isPlaying() {
        return gamePlayer != null;
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
    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BridgePlayer{");
        sb.append("playerUid=").append(playerUid);
        sb.append(", scoreMap=").append(scoreMap);
        sb.append('}');
        return sb.toString();
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
}
