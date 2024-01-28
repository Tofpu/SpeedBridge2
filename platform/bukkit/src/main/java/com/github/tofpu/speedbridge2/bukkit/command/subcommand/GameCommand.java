package com.github.tofpu.speedbridge2.bukkit.command.subcommand;

import com.github.tofpu.speedbridge2.bukkit.BukkitMessages;
import com.github.tofpu.speedbridge2.bukkit.command.annotation.RequireLobbyToBeAvailable;
import com.github.tofpu.speedbridge2.bukkit.util.MessageBuilder;
import com.github.tofpu.speedbridge2.common.game.BridgeSystem;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.help.CommandHelp;

class GameCommand extends CoreCommand {
    private final BridgeSystem bridgeSystem;

    public GameCommand(BridgeSystem bridgeSystem) {
        this.bridgeSystem = bridgeSystem;
    }

    @Subcommand("join")
    @RequireLobbyToBeAvailable
    public void gameJoin(final OnlinePlayer player, final Island island) {
        bridgeSystem.joinGame(player, island);
    }

    @Subcommand("end")
    @RequireLobbyToBeAvailable
    public void gameEnd(final OnlinePlayer player) {
        if (!bridgeSystem.isInGame(player.id())) {
            player.sendMessage(BukkitMessages.NOT_IN_GAME);
            return;
        }
        bridgeSystem.leaveGame(player.id());
        player.sendMessage(BukkitMessages.LEFT_GAME);
    }

    @Override
    public @NotNull String name() {
        return "game";
    }
}
