package io.tofpu.speedbridge2.game.infra.listener.equipment;

import io.github.revxrsal.eventbus.EventBus;
import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.game.domain.event.GameStartEvent;
import io.tofpu.speedbridge2.game.domain.event.GameStopEvent;
import io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.GameEquipmentHandler;

public class GameEquipmentLifecycle {
    private final GameEquipmentHandler equipmentHandler;

    public GameEquipmentLifecycle(GameEquipmentHandler equipmentHandler) {
        this.equipmentHandler = equipmentHandler;
    }

    public void register(EventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onGameStart(GameStartEvent event) {
        equipmentHandler.equip(event.getGame().gamePlayer().player());
    }

    @SubscribeEvent
    public void onGameStop(GameStopEvent event) {
        equipmentHandler.unequip(event.getGame().gamePlayer().player());
    }
}
