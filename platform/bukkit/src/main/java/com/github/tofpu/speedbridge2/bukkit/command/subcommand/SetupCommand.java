package com.github.tofpu.speedbridge2.bukkit.command.subcommand;

import com.github.tofpu.speedbridge2.bukkit.BukkitMessages;
import com.github.tofpu.speedbridge2.bukkit.command.annotation.RequireLobbyToBeAvailable;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Subcommand;

class SetupCommand extends CoreCommand {
    private final GameSetupSystem setupSystem;

    public SetupCommand(GameSetupSystem setupSystem) {
        this.setupSystem = setupSystem;
    }

    @Subcommand("create")
    @AutoComplete("* @schematics")
    @RequireLobbyToBeAvailable
    public void gameSetup(final OnlinePlayer player, final int slot, final String schematicName) {
        if (setupSystem.isInSetup(player.id())) {
            player.sendMessage(BukkitMessages.GAME_SETUP_PLAYER_BUSY);
            return;
        }
        setupSystem.startSetup(player, slot, schematicName);
        player.sendMessage(BukkitMessages.GAME_SETUP_CREATED, slot);
    }

    @Subcommand("cancel")
    @RequireLobbyToBeAvailable
    public void gameSetupCancel(final OnlinePlayer player) {
        int setupSlot = setupSystem.getSetupSlot(player.id());
        if (setupSlot == -1) {
            player.sendMessage(BukkitMessages.GAME_SETUP_PLAYER_MISSING);
            return;
        }

        setupSystem.cancelSetup(player);
        player.sendMessage(BukkitMessages.GAME_SETUP_CANCELLED, setupSlot);
    }

    @Override
    public @NotNull String name() {
        return "setup";
    }
}
