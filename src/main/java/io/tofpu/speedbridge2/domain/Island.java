package io.tofpu.speedbridge2.domain;

import io.tofpu.speedbridge2.database.Databases;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public final class Island {
    private final int slot;
    private final Map<GamePlayer, GameIsland> islandMap = new HashMap<>();
    private String category;

    public Island(final int slot, final String category) {
        this.slot = slot;
        this.category = category;
    }

    public Map.Entry<GamePlayer, GameIsland> generateGame(final Player player) {
        final GamePlayer gamePlayer = GamePlayer.of(player);
        final GameIsland gameIsland = new GameIsland(this.slot, gamePlayer);

        this.islandMap.put(gamePlayer, gameIsland);
        return new AbstractMap.SimpleImmutableEntry<>(gamePlayer, gameIsland);
    }

    public GameIsland findGameByPlayer(final GamePlayer gamePlayer) {
        return this.islandMap.get(gamePlayer);
    }

    public void setCategory(final String anotherCategory) {
        this.category = anotherCategory;
        update();
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
        sb.append(", category='").append(category).append('\'');
        sb.append(", islandMap=").append(islandMap);
        sb.append('}');
        return sb.toString();
    }
}
