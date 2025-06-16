package io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar;

import io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.item.GameItem;
import io.tofpu.toolbar.toolbar.GenericToolbar;
import io.tofpu.toolbar.toolbar.ToolWithSlot;

public class GameToolbar extends GenericToolbar<GameItem> {
    public GameToolbar(String identifier) {
        super(identifier);
    }

    public GameToolbar(String identifier, ToolWithSlot<GameItem>... toolWithSlots) {
        super(identifier, toolWithSlots);
    }
}
