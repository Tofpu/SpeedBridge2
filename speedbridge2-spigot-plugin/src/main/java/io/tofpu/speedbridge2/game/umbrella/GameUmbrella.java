package io.tofpu.speedbridge2.game.umbrella;

import io.tofpu.speedbridge2.game.GameSession;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.umbrella.RunCommandItemAction;
import io.tofpu.speedbridge2.model.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.umbrella.UmbrellaAPI;
import io.tofpu.umbrella.domain.Umbrella;
import io.tofpu.umbrella.domain.item.UmbrellaItem;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.inventory.ItemStack;

public class GameUmbrella {
    private final Umbrella umbrella;
    public GameUmbrella(final GameSession gameSession) {
        this.umbrella = UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaFactory()
                .create("island");

        for (final SerializableUmbrellaItem serializableUmbrellaItem : ConfigurationManager
                .INSTANCE.getGameCategory().getUmbrellaItems()) {
            // serializing SerializableUmbrellaItem to UmbrellaItem
            toUmbrellaItem(umbrella, serializableUmbrellaItem, gameSession);
        }
    }
    private UmbrellaItem toUmbrellaItem(final Umbrella umbrella, final SerializableUmbrellaItem serializableUmbrellaItem, GameSession gameSession) {
        final String identifier = serializableUmbrellaItem.getIdentifier();
        final ItemStack itemStack = serializableUmbrellaItem.getItemStack();
        final int index = serializableUmbrellaItem.getIndex();
        AbstractItemAction itemAction = serializableUmbrellaItem.getItemAction();

        if (itemAction instanceof RunCommandItemAction) {
            final RunCommandItemAction commandItemAction = (RunCommandItemAction) itemAction;
            itemAction = new RunCommandItemAction2(commandItemAction.getCommand(), gameSession);
        }

        return UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaItemFactory()
                .create(umbrella, identifier, itemStack, index, itemAction);
    }

    public Umbrella getUmbrella() {
        return umbrella;
    }
}
