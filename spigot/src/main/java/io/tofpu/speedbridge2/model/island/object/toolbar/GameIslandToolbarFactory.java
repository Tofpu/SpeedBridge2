package io.tofpu.speedbridge2.model.island.object.toolbar;

import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.toolbar.SerializableToolbarItem;
import io.tofpu.speedbridge2.model.common.util.ToolbarUtil;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.toolbar.ToolbarAPI;
import io.tofpu.toolbar.toolbar.SimpleToolbar;

import java.util.UUID;

public final class GameIslandToolbarFactory {

    public SimpleToolbar createAndRegisterToolbar(GameIsland gameIsland) {
        SimpleToolbar toolbar = new SimpleToolbar(
                "game_island_toolbar_" + UUID.randomUUID()
        );
        ToolbarAPI.getInstance().registerToolbar(toolbar);

        for (final SerializableToolbarItem serializableToolbarItem : ConfigurationManager
                .INSTANCE.getGameCategory().getToolbarItems()) {
            toolbar.addItem(
                    ToolbarUtil.toTool(gameIsland, serializableToolbarItem)
            );
        }
        return toolbar;
    }
}
