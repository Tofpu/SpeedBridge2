package io.tofpu.speedbridge2.domain.player.object;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.misc.sessional.SessionScore;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStat;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStatType;
import io.tofpu.speedbridge2.domain.player.object.extra.CommonBridgePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public final class BridgePlayer extends CommonBridgePlayer<Player> implements SessionScore {
    private final UUID playerUid;
    private final Map<Integer, Score> scoreMap;
    private final Map<String, PlayerStat> statsMap;

    private final Map<Integer, Score> sessionMap;

    private String name;
    private Player player;
    private GamePlayer gamePlayer;

    public static BridgePlayer of(final BridgePlayer copy) {
        return new BridgePlayer(copy);
    }

    public static BridgePlayer of(final UUID playerUid) {
        return new BridgePlayer(playerUid);
    }

    public static BridgePlayer of(final String name, final UUID playerUid) {
        return new BridgePlayer(name, playerUid);
    }

    private BridgePlayer(final BridgePlayer copy) {
        this(copy.getName(), copy.playerUid);
        this.scoreMap.putAll(copy.scoreMap);
        this.statsMap.putAll(copy.statsMap);
        this.sessionMap.putAll(copy.sessionMap);
    }

    private BridgePlayer(final UUID playerUid) {
        this("null", playerUid);

        if (player != null) {
            this.name = player.getName();
        }
    }

    private BridgePlayer(final String name, final UUID playerUid) {
        this.playerUid = playerUid;
        this.scoreMap = new HashMap<>();
        this.statsMap = new HashMap<>();
        this.sessionMap = new HashMap<>();

        this.name = name;
        if (playerUid != null) {
            this.player = Bukkit.getPlayer(playerUid);
        } else {
            this.player = null;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Score setScoreIfLower(final int islandSlot, final double score) {
        final Score currentScore = this.scoreMap.get(islandSlot);
        final Score newScore = Score.of(islandSlot, score);

        // adding the score to the session map
        final Score sessionScore = this.sessionMap.get(islandSlot);
        // if the session score is null, or the newer score is lower than the session score
        if (sessionScore == null || newScore.compareTo(sessionScore) < 0) {
            this.sessionMap.put(islandSlot, newScore);
        }

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
            Databases.SCORE_DATABASE.update(this.playerUid, score);
        } else {
            // otherwise, insert the score
            Databases.SCORE_DATABASE.insert(this.playerUid, score);
        }

        this.scoreMap.put(score.getScoredOn(), score);
        return score;
    }

    public Score setNewScore(final int islandSlot, final double newScore) {
        final Score score = Score.of(islandSlot, newScore);
        return setNewScore(score);
    }

    public void setInternalStat(final PlayerStat internalStat) {
        this.statsMap.put(internalStat.getKey()
                .toUpperCase(Locale.ENGLISH), internalStat);
    }

    public PlayerStat increment(final PlayerStatType playerStatType) {
        final PlayerStat playerStat = findStatBy(playerStatType);

        // this shouldn't be null
        if (playerStat != null) {
            playerStat.increment();
        }

        return playerStat;
    }

    public PlayerStat findStatBy(final PlayerStatType playerStatType) {
        return statsMap.computeIfAbsent(playerStatType.name(), s -> {
            final PlayerStat stat = PlayerStatType.create(getPlayerUid(),
                    playerStatType);
            Databases.STATS_DATABASE.insert(stat);

            return stat;
        });
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

    public Iterable<? extends PlayerStat> getStats() {
        return this.statsMap.values();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void invalidatePlayer() {
        this.player = null;

        // resetting the session scores
        resetSessionScores();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void internalRefresh(final UUID uniqueId) {
        this.player = Bukkit.getPlayer(uniqueId);
        this.name = player.getName();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BridgePlayer{");
        sb.append("playerUid=")
                .append(playerUid);
        sb.append(", scoreMap=")
                .append(scoreMap);
        sb.append(", statsMap=")
                .append(statsMap);
        sb.append(", player=")
                .append(player);
        sb.append(", gamePlayer=")
                .append(gamePlayer);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Collection<Score> getSessionScores() {
        return Collections.unmodifiableCollection(this.sessionMap.values());
    }

    @Override
    public void resetSessionScores() {
        this.sessionMap.clear();
    }
}
