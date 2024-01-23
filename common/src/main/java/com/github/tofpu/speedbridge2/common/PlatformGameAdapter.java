package com.github.tofpu.speedbridge2.common;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGamePlayer;

public interface PlatformGameAdapter {
    static PlatformGameAdapter empty() {
        return new PlatformGameAdapter() {
            @Override
            public void onGamePrepare(IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void onGameReset(IslandGame game, IslandGamePlayer player) {
            }

            @Override
            public void onGameStop(IslandGame game, IslandGamePlayer player) {
            }
        };
    }

    void onGamePrepare(IslandGame game, IslandGamePlayer player);

    void onGameReset(IslandGame game, IslandGamePlayer player);

    void onGameStop(IslandGame game, IslandGamePlayer player);
}