package io.tofpu.speedbridge2.domain;

import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public final class Island {
    private final int slot;
    private final Map<GamePlayer, GameIsland> islandMap = new HashMap<>();

    public Island(final int slot) {
        this.slot = slot;
    }

    public Map.Entry<GamePlayer, GameIsland> generateGame(final Player player) {
        final GamePlayer gamePlayer = new GamePlayer(player);
        final GameIsland gameIsland = new GameIsland(this.slot, gamePlayer);

        this.islandMap.put(gamePlayer, gameIsland);

        return new AbstractMap.SimpleImmutableEntry<>(gamePlayer, gameIsland);
    }

    public GameIsland findGameByPlayer(final GamePlayer gamePlayer) {
        return this.islandMap.get(gamePlayer);
    }

    public int getSlot() {
        return slot;
    }
}
