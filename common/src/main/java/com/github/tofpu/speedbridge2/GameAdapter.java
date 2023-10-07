package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;

public interface GameAdapter {
    static GameAdapter empty() {
        return new GameAdapter() {
            @Override
            public void prepareGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void cleanGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void resetGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
            }
        };
    }

    void prepareGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player);

    void cleanGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player);

    void resetGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player);
}
