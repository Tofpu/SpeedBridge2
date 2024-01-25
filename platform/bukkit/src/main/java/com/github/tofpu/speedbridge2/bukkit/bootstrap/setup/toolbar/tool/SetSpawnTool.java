package com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.tool;

import com.cryptomorin.xseries.XMaterial;
import com.github.tofpu.speedbridge2.bukkit.BukkitMessages;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.SetupTool;
import com.github.tofpu.speedbridge2.bukkit.helper.CoreConversionHelper;
import com.github.tofpu.speedbridge2.bukkit.util.ItemStackBuilder;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import org.bukkit.inventory.ItemStack;

public class SetSpawnTool extends SetupTool {
    private static final ItemStack TOOL_ITEM = ItemStackBuilder.newBuilder()
            .withDisplayName("&eSet the Player Spawn")
            .addLore("&7Click to set the player spawn-point!")
            .build(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE);

    public SetSpawnTool(GameSetupSystem setupSystem) {
        super("set_spawn", TOOL_ITEM, event -> {
            IslandSetup setup = setupSystem.getSetup(event.getPlayer().getUniqueId());
            if (setup == null) return;
            setup.data().origin(CoreConversionHelper.toLocation(event.getPlayer().getLocation()));
            event.getPlayer().sendMessage(BukkitMessages.GAME_SETUP_SET_SPAWNPOINT.defaultMessage());
        });
    }
}
