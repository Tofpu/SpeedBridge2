package io.tofpu.speedbridge2.domain.island.object;

import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.island.schematic.IslandSchematic;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Island extends IslandSchematic {
    private final int slot;

    private final Map<GamePlayer, GameIsland> islandMap = new HashMap<>();
    private String category;

    public Island(final int slot, final String category) {
        this.slot = slot;
        this.category = category;
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

    public boolean selectSchematic(final String schematicName) {
        final boolean successful = super.selectSchematic(schematicName);
        // if the operation was successful, update the database
        if (successful) {
            update();
        }
        return successful;
    }

    public boolean isReady() {
        return getSchematicClipboard() != null;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Island{");
        sb.append("slot=").append(slot);
        sb.append(", islandMap=").append(islandMap);
        sb.append(", category='").append(category).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
