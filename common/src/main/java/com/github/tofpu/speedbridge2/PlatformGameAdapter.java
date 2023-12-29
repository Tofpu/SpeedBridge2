package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;

public interface PlatformGameAdapter {
    static PlatformGameAdapter empty() {
        return new PlatformGameAdapter() {
            @Override
            public void prepareGame(IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void cleanGame(IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void resetGame(IslandGame game, IslandGamePlayer player) {
            }
        };
    }

    void prepareGame(IslandGame game, IslandGamePlayer player);

    void cleanGame(IslandGame game, IslandGamePlayer player);

    void resetGame(IslandGame game, IslandGamePlayer player);
}
