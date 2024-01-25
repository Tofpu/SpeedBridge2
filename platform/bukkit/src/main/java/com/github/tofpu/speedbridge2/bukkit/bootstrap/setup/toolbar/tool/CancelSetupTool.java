package com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.tool;

import com.cryptomorin.xseries.XMaterial;
import com.github.tofpu.speedbridge2.bukkit.BukkitMessages;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.SetupTool;
import com.github.tofpu.speedbridge2.bukkit.util.ItemStackBuilder;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupStates;
import org.bukkit.inventory.ItemStack;

public class CancelSetupTool extends SetupTool {
    private static final ItemStack TOOL_ITEM = ItemStackBuilder.newBuilder()
            .withDisplayName("&eCancel the Setup")
            .addLore("&7Click to cancel the island setup!")
            .build(XMaterial.RED_DYE);

    public CancelSetupTool(GameSetupSystem setupSystem) {
        super("cancel", TOOL_ITEM , event -> {
            IslandSetup setup = setupSystem.getSetup(event.getPlayer().getUniqueId());
            if (setup == null) return;

            setup.data().cancelled(true);
            event.getPlayer().sendMessage(BukkitMessages.GAME_SETUP_CANCELLED.defaultMessage(setup.data().slot()));
            setup.dispatch(IslandSetupStates.STOP);
        });
    }
}
