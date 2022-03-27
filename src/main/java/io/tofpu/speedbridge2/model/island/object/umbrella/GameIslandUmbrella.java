package io.tofpu.speedbridge2.model.island.object.umbrella;

import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.speedbridge2.model.common.util.UmbrellaUtil;
import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;
import io.tofpu.umbrella.UmbrellaAPI;
import io.tofpu.umbrella.domain.Umbrella;

public final class GameIslandUmbrella {
    private final Umbrella umbrella;

    public GameIslandUmbrella(final GameIsland gameIsland) {
        this.umbrella = UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaFactory()
                .create("island");

        for (final SerializableUmbrellaItem serializableUmbrellaItem : ConfigurationManager
                .INSTANCE.getGameCategory().getUmbrellaItems()) {
            // serializing SerializableUmbrellaItem to UmbrellaItem
            UmbrellaUtil.toUmbrellaItem(umbrella, gameIsland, serializableUmbrellaItem);
        }
    }

    public Umbrella getUmbrella() {
        return umbrella;
    }
}
