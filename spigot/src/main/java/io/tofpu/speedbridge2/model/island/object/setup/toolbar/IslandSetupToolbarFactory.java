package io.tofpu.speedbridge2.model.island.object.setup.toolbar;

import com.cryptomorin.xseries.XMaterial;
import com.tofpu.toolbar.api.ToolbarAPI;
import com.tofpu.toolbar.api.toolbar.ItemSlot;
import com.tofpu.toolbar.api.toolbar.Toolbar;
import com.tofpu.toolbar.api.toolbar.ToolbarSchema;
import com.tofpu.toolbar.api.toolbar.tool.Tool;
import com.tofpu.toolbar.api.toolbar.tool.action.ToolAction;
import io.tofpu.speedbridge2.model.common.util.ToolbarUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class IslandSetupToolbarFactory {
    public Toolbar createAndRegisterToolbar() {
        ToolbarSchema.Builder toolbarSchemaBuilder = ToolbarSchema.newBuilder("island_setup");

        final ItemStack finishItem = ToolbarUtil.create(XMaterial.GREEN_DYE,
                "Complete the " + "Island Setup", "complete the island setup!");
        toolbarSchemaBuilder.addItems(Tool.of(
                "finish",
                finishItem,
                ToolAction.of(PlayerInteractEvent.class, (genericToolbar, event) -> {
                    event.getPlayer()
                            .performCommand("sb setup finish");
                }),
                ItemSlot.atIndex(3)
        ));

        final ItemStack setSpawnItem = ToolbarUtil.create(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE,
                "Set the Island Spawnpoint", "set the island spawn-point!");
        toolbarSchemaBuilder.addItems(Tool.of(
                "set_spawn",
                setSpawnItem,
                ToolAction.of(
                        PlayerInteractEvent.class,
                        (genericToolbar, event) ->
                                event.getPlayer().performCommand("sb setup setspawn")),
                ItemSlot.atIndex(4)
        ));

        final ItemStack cancelItem = ToolbarUtil.create(XMaterial.RED_DYE,
                "Cancel the " + "Island Setup", "cancel the island setup!");
        toolbarSchemaBuilder.addItems(Tool.of(
                "cancel",
                cancelItem,
                ToolAction.of(
                        PlayerInteractEvent.class,
                        (genericToolbar, event) ->
                                event.getPlayer().performCommand("sb setup cancel")),
                ItemSlot.atIndex(5)
        ));

        ToolbarSchema toolbarSchema = toolbarSchemaBuilder.build();
        return ToolbarAPI.getInstance().toolbarService().register(toolbarSchema);
    }
}
