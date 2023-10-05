package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.bridge.game.BridgeGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;

public interface GameAdapter {
    static GameAdapter empty() {
        return new GameAdapter() {
            @Override
            public void prepareGame(BridgeGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void cleanGame(BridgeGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void resetGame(BridgeGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
            }
        };
    }

    void prepareGame(BridgeGameHandler gameHandler, IslandGame game, IslandGamePlayer player);

    void cleanGame(BridgeGameHandler gameHandler, IslandGame game, IslandGamePlayer player);

    void resetGame(BridgeGameHandler gameHandler, IslandGame game, IslandGamePlayer player);
}
