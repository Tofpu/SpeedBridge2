package io.tofpu.speedbridge2.block.infra.listener;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.block.service.BlockService;
import io.tofpu.speedbridge2.game.domain.event.GameStartEvent;

public class GameStateListener {
    private final BlockService blockService;

    public GameStateListener(BlockService blockService) {
        this.blockService = blockService;
    }

    @SubscribeEvent
    public void on(GameStartEvent event) {
        blockService.spawnBlock(event.getGame().gamePlayer().player());
    }
}
