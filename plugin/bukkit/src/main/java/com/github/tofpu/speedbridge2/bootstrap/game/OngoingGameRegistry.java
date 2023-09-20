package com.github.tofpu.speedbridge2.bootstrap.game;

import com.github.tofpu.speedbridge2.game.island.IslandGame;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingGameRegistry {
    private final Map<UUID, IslandGame> gameMap = new HashMap<>();

    public void add(UUID id, IslandGame game) {
        this.gameMap.put(id, game);
    }

    public IslandGame get(UUID id) {
        return this.gameMap.get(id);
    }

    public void remove(UUID id) {
        gameMap.remove(id);
    }
}