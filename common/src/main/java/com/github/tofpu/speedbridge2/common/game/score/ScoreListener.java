package com.github.tofpu.speedbridge2.common.game.score;

import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.event.PlayerJoinEvent;

class ScoreListener implements Listener {
    private final BridgeScoreService scoreService;

    ScoreListener(BridgeScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @EventListener
    void on(final PlayerJoinEvent event) {
        scoreService.handleJoin(event.getPlayer().id());
    }
}
