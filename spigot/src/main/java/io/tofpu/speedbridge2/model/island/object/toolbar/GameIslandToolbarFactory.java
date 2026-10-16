package io.tofpu.speedbridge2.model.island.object.toolbar;

import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.toolbar.SerializableToolbarItem;
import io.tofpu.speedbridge2.model.common.util.ToolbarUtil;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import com.tofpu.toolbar.api.ToolbarAPI;
import com.tofpu.toolbar.api.toolbar.Toolbar;
import com.tofpu.toolbar.api.toolbar.ToolbarSchema;

import java.util.UUID;

public final class GameIslandToolbarFactory {

    public Toolbar createAndRegisterToolbar(GameIsland gameIsland) {
        ToolbarSchema.Builder toolbarSchemaBuilder = ToolbarSchema.newBuilder("game_island_toolbar_" + UUID.randomUUID());

        for (final SerializableToolbarItem serializableToolbarItem : ConfigurationManager
                .INSTANCE.getGameCategory().getToolbarItems()) {
            toolbarSchemaBuilder.addItems(
                    ToolbarUtil.toTool(gameIsland, serializableToolbarItem)
            );
        }
        ToolbarSchema toolbarSchema = toolbarSchemaBuilder.build();
        return ToolbarAPI.getInstance().toolbarService().register(toolbarSchema);
    }
}
