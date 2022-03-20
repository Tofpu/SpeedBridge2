package io.tofpu.speedbridge2.domain.player.object;

import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.extra.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.IslandBoard;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.misc.block.BlockChoice;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.misc.session.SessionScore;
import io.tofpu.speedbridge2.domain.player.misc.setup.SetupMeta;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStat;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStatType;
import io.tofpu.speedbridge2.domain.player.object.extra.CommonBridgePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BridgePlayer extends CommonBridgePlayer<Player> implements SessionScore, BlockChoice, SetupMeta {
    private final UUID playerUid;
    private final Map<Integer, Score> scoreMap;
    private final Map<String, PlayerStat> statsMap;

    private final Map<Integer, Score> sessionMap;

    private String name;
    private Player player;
    private GamePlayer gamePlayer;

    private Material chosenBlock;
    private boolean inSetup;

    /**
     * Create a new BridgePlayer object that is a copy of the given BridgePlayer
     *
     * @param copy The BridgePlayer to copy.
     * @return A new BridgePlayer object.
     */
    public static BridgePlayer of(final BridgePlayer copy) {
        return new BridgePlayer(copy);
    }

    /**
     * Create a new BridgePlayer object with the given UUID
     *
     * @param playerUid The UUID of the player.
     * @return A new BridgePlayer object.
     */
    public static BridgePlayer of(final UUID playerUid) {
        return new BridgePlayer(playerUid);
    }

    /**
     * Create a new BridgePlayer object with the given name and playerUid
     *
     * @param name The name of the player.
     * @param playerUid The UUID of the player.
     * @return A new BridgePlayer object.
     */
    public static BridgePlayer of(final String name, final UUID playerUid) {
        return new BridgePlayer(name, playerUid);
    }

    private BridgePlayer(final BridgePlayer copy) {
        this(copy.getName(), copy.playerUid);
        this.scoreMap.putAll(copy.scoreMap);
        this.statsMap.putAll(copy.statsMap);

        this.sessionMap.putAll(copy.sessionMap);
        this.chosenBlock = copy.chosenBlock;
    }

    protected BridgePlayer(final UUID playerUid) {
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

        this.chosenBlock =
                ConfigurationManager.INSTANCE.getBlockMenuCategory().getDefaultBlock();
        this.inSetup = false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * If the score is lower than the current score,
     * then update the scoreMap and sessionMap
     *
     * @param islandSlot The slot of the island that the player is currently on.
     * @param score the score to be set
     * @return The new score.
     */
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

    /**
     * It sets the internal score map to the score passed in.
     *
     * @param score The score to be added to the internal map.
     * @return The score that was just added to the map.
     */
    public Score setInternalNewScore(final Score score) {
        this.scoreMap.put(score.getScoredOn(), score);
        return score;
    }

    /**
     * Add a score to the player's score map and the global leaderboard
     *
     * @param score The score object that you want to add to the player's score list.
     * @return The score that was just added to the player's score map.
     */
    public Score setNewScore(final Score score) {
        // if our score map contains the island, update the score
        if (this.scoreMap.containsKey(score.getScoredOn())) {
            Databases.SCORE_DATABASE.update(this.playerUid, score);
        } else {
            // otherwise, insert the score
            Databases.SCORE_DATABASE.insert(this.playerUid, score);
        }

        // adding the score to the global leaderboard
        Leaderboard.INSTANCE.addScore(this, score);

        // adding the score to island leaderboard
        final Island island = IslandService.INSTANCE.findIslandBy(score.getScoredOn());
        if (island != null) {
            island.addLeaderboardScore(this, score);
        }

        this.scoreMap.put(score.getScoredOn(), score);
        return score;
    }

    /**
     * It sets the score of the given island slot to the given new score.
     *
     * @param islandSlot The island slot that the score is being set for.
     * @param newScore The new score to set.
     * @return the score
     */
    public Score setNewScore(final int islandSlot, final double newScore) {
        final Score score = Score.of(islandSlot, newScore);
        return setNewScore(score);
    }

    /**
     * This function takes in a PlayerStat object and adds it to the statsMap
     *
     * @param internalStat The internal stat to set.
     */
    public void setInternalStat(final PlayerStat internalStat) {
        this.statsMap.put(internalStat.getKey()
                .toUpperCase(Locale.ENGLISH), internalStat);
    }

    /**
     * Increment the value of a player stat
     *
     * @param playerStatType The type of stat to increment.
     * @return Nothing.
     */
    public PlayerStat increment(final PlayerStatType playerStatType) {
        final PlayerStat playerStat = findStatBy(playerStatType);

        // this shouldn't be null
        if (playerStat != null) {
            playerStat.increment();
        }

        return playerStat;
    }

    /**
     * Given a playerStatType, find the stat for that playerStatType. If it doesn't exist,
     * create it
     *
     * @param playerStatType The type of stat you want to find.
     * @return A PlayerStat object.
     */
    public PlayerStat findStatBy(final PlayerStatType playerStatType) {
        return statsMap.computeIfAbsent(playerStatType.name(), s -> {
            final PlayerStat stat = PlayerStatType.create(getPlayerUid(), playerStatType);
            Databases.STATS_DATABASE.insert(stat);

            return stat;
        });
    }

    /**
     * This function resets the player's stats, score, and session data
     *
     * @return a completable future when the player is wiped from the database
     */
    public CompletableFuture<Void> reset() {
        this.scoreMap.clear();
        this.statsMap.clear();
        this.sessionMap.clear();
        this.chosenBlock = ConfigurationManager.INSTANCE.getBlockMenuCategory()
                .getDefaultBlock();

        Leaderboard.INSTANCE.reset(getPlayerUid());
        IslandBoard.reset(getPlayerUid());

        return Databases.PLAYER_DATABASE.delete(getPlayerUid());
    }

    public void setGamePlayer(final GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public boolean isPlaying() {
        return gamePlayer != null;
    }

    /**
     * Find the score for the given island slot
     *
     * @param islandSlot The slot of the island that the score is for.
     * @return The Score object that is associated with the given islandSlot.
     */
    public Score findScoreBy(final int islandSlot) {
        return scoreMap.get(islandSlot);
    }

    /**
     * @return the player unique id
     */
    public UUID getPlayerUid() {
        return playerUid;
    }

    public Collection<Score> getScores() {
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

        if (isPlaying()) {
            leaveGame();
        }

        // resetting the island setup
        resetSetup();

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
    public Collection<Score> getSessionScores() {
        return Collections.unmodifiableCollection(this.sessionMap.values());
    }

    @Override
    public void resetSessionScores() {
        this.sessionMap.clear();
    }

    @Override
    public Material getChoseMaterial() {
        return this.chosenBlock;
    }

    @Override
    public void setChosenMaterial(final @NotNull Material material) {
        this.chosenBlock = material;

        // if the player is playing, update their block with the new material
        if (isPlaying()) {
            getPlayer().getInventory().setItem(0, new ItemStack(this.chosenBlock, 64));
        }

        // this may cause the database to lock if it got abused, we'll see
        Databases.BLOCK_DATABASE.update(this);
    }

    public void setIntervalMaterial(final @NotNull Material material) {
        this.chosenBlock = material;
    }

    @Override
    public boolean isInSetup() {
        return this.inSetup;
    }

    @Override
    public void toggleSetup() {
        this.inSetup = !this.inSetup;
    }

    @Override
    public void resetSetup() {
        this.inSetup = false;
    }

    /**
     * Returns the timer value of the game player
     *
     * @return The timer value of the game player.
     */
    public long getTimer() {
        if (gamePlayer == null) {
            return -1;
        }
        return gamePlayer.getTimer();
    }

    /**
     * Leave the current game if they're in one
     */
    public void leaveGame() {
        final GameIsland currentGame = getCurrentGame();
        if (currentGame == null) {
            return;
        }
        currentGame.stopGame();
    }

    /**
     * Returns the current game that the player is playing
     *
     * @return The current game that the player is playing.
     */
    public GameIsland getCurrentGame() {
        if (gamePlayer == null) {
            return null;
        }
        return gamePlayer.getCurrentGame();
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
        sb.append(", sessionMap=")
                .append(sessionMap);
        sb.append(", name='")
                .append(name)
                .append('\'');
        sb.append(", player=")
                .append(player);
        sb.append(", gamePlayer=")
                .append(gamePlayer);
        sb.append(", chosenBlock=")
                .append(chosenBlock);
        sb.append(", setup=")
                .append(inSetup);
        sb.append('}');
        return sb.toString();
    }
}
