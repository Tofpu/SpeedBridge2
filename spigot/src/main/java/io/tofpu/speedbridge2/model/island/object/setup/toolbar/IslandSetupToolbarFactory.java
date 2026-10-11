package io.tofpu.speedbridge2.model.island.object.setup.toolbar;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.model.common.util.ToolbarUtil;
import io.tofpu.toolbar.ToolbarAPI;
import io.tofpu.toolbar.toolbar.ItemSlot;
import io.tofpu.toolbar.toolbar.SimpleToolbar;
import io.tofpu.toolbar.toolbar.ToolWithSlot;
import io.tofpu.toolbar.toolbar.tool.Tool;
import io.tofpu.toolbar.toolbar.tool.action.ToolActionUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class IslandSetupToolbarFactory {
    public SimpleToolbar createAndRegisterToolbar() {
        SimpleToolbar toolbar = new SimpleToolbar("island_setup");
        ToolbarAPI.getInstance().registerToolbar(toolbar);

        final ItemStack finishItem = ToolbarUtil.create(XMaterial.GREEN_DYE,
                "Complete the " + "Island Setup", "complete the island setup!");
        toolbar.addItem(new ToolWithSlot<>(
                new Tool(
                        "finish",
                        finishItem,
                        ToolActionUtil.listenFor(PlayerInteractEvent.class, (genericToolbar, event) -> {
                            event.getPlayer()
                                    .performCommand("sb setup finish");
                        })
                ),
                ItemSlot.atIndex(3)
        ));

        final ItemStack setSpawnItem = ToolbarUtil.create(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE,
                "Set the Island Spawnpoint", "set the island spawn-point!");
        toolbar.addItem(new ToolWithSlot<>(
                new Tool(
                        "set_spawn",
                        setSpawnItem,
                        ToolActionUtil.listenFor(
                                PlayerInteractEvent.class,
                                (genericToolbar, event) ->
                                        event.getPlayer().performCommand("sb setup setspawn"))
                ),
                ItemSlot.atIndex(4)
        ));

        final ItemStack cancelItem = ToolbarUtil.create(XMaterial.RED_DYE,
                "Cancel the " + "Island Setup", "cancel the island setup!");
        toolbar.addItem(new ToolWithSlot<>(
                new Tool(
                        "cancel",
                        cancelItem,
                        ToolActionUtil.listenFor(
                                PlayerInteractEvent.class,
                                (genericToolbar, event) ->
                                        event.getPlayer().performCommand("sb setup cancel"))
                ),
                ItemSlot.atIndex(5)
        ));

        return toolbar;
    }
}
