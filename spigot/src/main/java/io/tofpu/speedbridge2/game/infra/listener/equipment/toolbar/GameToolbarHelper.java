package io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar;

import io.tofpu.speedbridge2.game.infra.config.item.GameHotbarConfiguration;
import io.tofpu.speedbridge2.game.infra.listener.equipment.toolbar.item.GameItem;
import io.tofpu.toolbar.toolbar.ItemSlot;
import io.tofpu.toolbar.toolbar.ToolWithSlot;
import java.util.function.Function;

public class GameToolbarHelper {
    public static ToolWithSlot<GameItem> toolItemMapper(
            GameHotbarConfiguration.Item config, Function<GameHotbarConfiguration.Item, GameItem> itemFunction) {
        ItemSlot slot = ItemSlot.atIndex(config.slot());
        GameItem gameItem = itemFunction.apply(config);
        return new ToolWithSlot<>(gameItem, slot);
    }
}
