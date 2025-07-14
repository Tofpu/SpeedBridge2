package io.tofpu.speedbridge2.scoreboard.infra.listener.game;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.game.domain.event.GameStartEvent;
import io.tofpu.speedbridge2.game.domain.event.GameStopEvent;
import io.tofpu.speedbridge2.scoreboard.service.ScoreboardService;

public class PlayerGameStateListener {
    private final ScoreboardService service;

    public PlayerGameStateListener(ScoreboardService service) {
        this.service = service;
    }

    @SubscribeEvent
    public void onGameStart(GameStartEvent event) {
        service.addPlayer(event.getGame().gamePlayer().player());
    }

    @SubscribeEvent
    public void onGameStop(GameStopEvent event) {
        service.removePlayer(event.getGame().gamePlayer().player());
    }
}
