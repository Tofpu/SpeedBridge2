package io.tofpu.speedbridge2.setup.infra.listener;

import com.cryptomorin.xseries.XMaterial;
import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.setup.service.IslandSetup;
import io.tofpu.speedbridge2.setup.service.SetupService;
import io.tofpu.speedbridge2.setup.domain.event.SetupStartEvent;
import io.tofpu.speedbridge2.setup.domain.event.SetupStopEvent;
import io.tofpu.speedbridge2.setup.infra.listener.tools.CancelTool;
import io.tofpu.speedbridge2.setup.infra.listener.tools.FinishTool;
import io.tofpu.speedbridge2.setup.infra.listener.tools.SetSpawnpointTool;
import io.tofpu.speedbridge2.setup.infra.listener.tools.SetupTool;
import io.tofpu.speedbridge2.util.ItemStackBuilder;
import io.tofpu.toolbar.ToolbarAPI;
import io.tofpu.toolbar.toolbar.GenericToolbar;
import io.tofpu.toolbar.toolbar.ItemSlot;
import io.tofpu.toolbar.toolbar.ToolWithSlot;

import java.util.List;

public class SetupToolsHandler {
    public static final String TOOLBAR_ID = "setup_toolbar";
    private final ToolbarAPI toolbarAPI;
    private final SetupService setupService;

    public SetupToolsHandler(ToolbarAPI toolbarAPI, SetupService setupService) {
        this.toolbarAPI = toolbarAPI;
        this.setupService = setupService;
    }

    public void registerToolbar() {
        GenericToolbar<SetupTool> genericToolbar = new GenericToolbar<>(TOOLBAR_ID, tools());
        this.toolbarAPI.registerToolbar(genericToolbar);
    }

    public ToolWithSlot<SetupTool>[] tools() {
        //noinspection unchecked
        return new ToolWithSlot[]{
                new ToolWithSlot<>(new SetSpawnpointTool(setupService, "set_spawn_point",
                        ItemStackBuilder.newBuilder()
                                .displayName("&aSet Spawnpoint")
                                .lore(List.of("&7Click to set the spawnpoint!"))
                                .apply(XMaterial.RED_BED.parseItem())
                ), ItemSlot.atIndex(4)),
                new ToolWithSlot<>(new FinishTool(setupService, "finish_setup",
                        ItemStackBuilder.newBuilder()
                                .displayName("&aFinish the Setup")
                                .lore(List.of("&7Click to finish the setup!"))
                                .apply(XMaterial.GREEN_DYE.parseItem())),
                        ItemSlot.atIndex(5)),
                new ToolWithSlot<>(new CancelTool(setupService, "cancel_setup",
                        ItemStackBuilder.newBuilder()
                                .displayName("&cCancel the Setup")
                                .lore(List.of("&7Click to cancel the setup!"))
                                .apply(XMaterial.RED_DYE.parseItem())),
                        ItemSlot.atIndex(3)
                )
        };
    }

    @SubscribeEvent
    public void equipTool(SetupStartEvent event) {
        IslandSetup setup = event.getSetup();
        toolbarAPI.equip(TOOLBAR_ID, setup.player());
    }

    @SubscribeEvent
    public void unequipTool(SetupStopEvent event) {
        IslandSetup setup = event.getSetup();
        toolbarAPI.unequip(setup.player());
    }
}
