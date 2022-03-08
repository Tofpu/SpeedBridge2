package io.tofpu.speedbridge2.domain.island.object;

import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.extra.leaderboard.LeaderboardMap;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.island.schematic.IslandSchematic;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Island extends IslandSchematic {
    private final int slot;
    private final Map<GamePlayer, GameIsland> islandMap = new HashMap<>();
    private String category;

    private final LeaderboardMap leaderboardMap = new LeaderboardMap();
    private Location absoluteLocation = null;

    public Island(final int slot, final String category) {
        this.slot = slot;
        this.category = category;
    }

    public BoardPlayer retrieveBy(final int position) {
        return leaderboardMap.get(position);
    }

    public BoardPlayer retrieveBy(final UUID uniqueId) {
        for (final BoardPlayer boardPlayer : leaderboardMap.values()) {
            if (boardPlayer.getOwner()
                    .equals(uniqueId)) {
                return boardPlayer;
            }
        }
        return null;
    }

    public Map.Entry<GamePlayer, GameIsland> generateGame(final BridgePlayer player) {
        // if a schematic cannot be found, return null
        if (getSchematicClipboard() == null) {
            return null;
        }

        final GamePlayer gamePlayer = GamePlayer.of(player);
        final GameIsland gameIsland = GameIsland.of(this, gamePlayer);
        player.setGamePlayer(gamePlayer);

        this.islandMap.put(gamePlayer, gameIsland);
        return new AbstractMap.SimpleImmutableEntry<>(gamePlayer, gameIsland);
    }

    public void leaveGame(final BridgePlayer bridgePlayer) {
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();
        final GameIsland gameIsland = this.islandMap.remove(gamePlayer);
        if (gameIsland == null) {
            return;
        }
        BridgeUtil.sendMessage(bridgePlayer.getPlayer(),
                String.format(Message.INSTANCE.LEFT_AN_ISLAND,
                slot));
        bridgePlayer.setGamePlayer(null);

        // remove the game player
        gamePlayer.remove();
        // reset the game island
        gameIsland.remove();
    }

    public GameIsland findGameByPlayer(final GamePlayer gamePlayer) {
        return this.islandMap.get(gamePlayer);
    }

    public void setCategory(final String anotherCategory) {
        this.category = anotherCategory;
        update();
    }

    public boolean selectSchematic(final @NotNull String schematicName) {
        final boolean successful = super.selectSchematic(schematicName);
        // if the operation was successful, update the database
        if (successful) {
            update();
        }
        return successful;
    }

    public void setAbsoluteLocation(final Location newAbsoluteLocation) {
        this.absoluteLocation = newAbsoluteLocation;
        update();
    }

    public Location getAbsoluteLocation() {
        return this.absoluteLocation;
    }

    public boolean isReady() {
        return getSchematicClipboard() != null && absoluteLocation != null;
    }

    private void update() {
        Databases.ISLAND_DATABASE.update(this);
    }

    public int getSlot() {
        return slot;
    }

    public String getCategory() {
        return category;
    }

    public void loadBoard(final Map<Integer, BoardPlayer> boardMap) {
        this.leaderboardMap.load(boardMap);
    }

    public void addLeaderboardScore(final BridgePlayer bridgePlayer, final Score score) {
        leaderboardMap.append(bridgePlayer, score);
    }

    public void updateLeaderboard() {
        this.leaderboardMap.updateLeaderboard();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Island{");
        sb.append("slot=")
                .append(slot);
        sb.append(", islandMap=")
                .append(islandMap);
        sb.append(", category='")
                .append(category)
                .append('\'');
        sb.append(", boardMap=")
                .append(leaderboardMap);
        sb.append('}');
        return sb.toString();
    }
}
