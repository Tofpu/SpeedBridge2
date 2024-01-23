package com.github.tofpu.speedbridge2.common.game.event;

import com.github.tofpu.speedbridge2.common.game.IslandGame;
import com.github.tofpu.speedbridge2.common.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.event.Cancellable;
import com.github.tofpu.speedbridge2.event.Event;

public class IslandGameEvent extends Event implements Cancellable {
    private final IslandGame game;
    private final IslandGamePlayer player;
    private boolean cancelled = false;

    public IslandGameEvent(IslandGame game, IslandGamePlayer player) {
        this.game = game;
        this.player = player;
    }

    public IslandGame game() {
        return game;
    }

    public IslandGamePlayer player() {
        return player;
    }

    @Override
    public void cancel(boolean state) {
        this.cancelled = state;
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }
}
