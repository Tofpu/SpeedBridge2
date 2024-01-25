package com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.tool;

import com.cryptomorin.xseries.XMaterial;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.SetupTool;
import com.github.tofpu.speedbridge2.bukkit.util.ItemStackBuilder;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupStates;
import org.bukkit.inventory.ItemStack;

public class SetupFinishTool extends SetupTool {
    private static final ItemStack TOOL_ITEM = ItemStackBuilder.newBuilder()
            .withDisplayName("&eComplete the Setup")
            .addLore("&7Click to complete the island setup!")
            .build(XMaterial.GREEN_DYE);

    public SetupFinishTool(GameSetupSystem setupSystem) {
        super("finish", TOOL_ITEM , event -> {
            IslandSetup setup = setupSystem.getSetup(event.getPlayer().getUniqueId());
            if (setup == null) return;
            setup.dispatch(IslandSetupStates.STOP);
        });
    }
}
