package com.github.tofpu.speedbridge2.bukkit.command;

import com.github.tofpu.speedbridge2.bukkit.command.annotation.RequireLobbyToBeAvailable;
import com.github.tofpu.speedbridge2.bukkit.plugin.BukkitPlugin;
import com.github.tofpu.speedbridge2.common.gameextra.land.PlayerLandReserver;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Command({"speedbridge dev", "sb dev"})
@CommandPermission("sb.dev")
public class DeveloperCommandHolder {
    private final IslandService islandService;
    private final PlayerLandReserver landReserver;

    private final List<Land> lands = new ArrayList<>();

    public DeveloperCommandHolder(BukkitPlugin plugin) {
        this(plugin.getService(IslandService.class), plugin.bridgeSystem().landReserver());
    }

    public DeveloperCommandHolder(IslandService islandService, PlayerLandReserver landReserver) {
        this.islandService = islandService;
        this.landReserver = landReserver;
    }

    @Subcommand("game create")
    @RequireLobbyToBeAvailable
    public void gameCreate(final OnlinePlayer player, final int slot, final @Default("1") int amount) {
        Island island = islandService.get(slot);
        if (island == null) {
            player.sendMessage("Unknown island: " + slot);
            return;
        }

        for (int i = 0; i < amount; i++) {
            lands.add(landReserver.reserveSpot(UUID.randomUUID(), island));
        }
        player.sendMessage(String.format("Generated %s amount of games", amount));
    }

    @Subcommand("game unlock")
    public void gameUnlock(final OnlinePlayer player, final @Default("1") int amount) {
        if (lands.size() == 0) {
            player.sendMessage("There is none games to unlock.");
            return;
        }
        int maximumAmount = Math.min(amount, lands.size());
        for (int i = 0; i < maximumAmount; i++) {
            landReserver.releaseSpot(lands.remove(0));
        }
        player.sendMessage(String.format("Unlocked %s amount of games", maximumAmount));
    }

    @Subcommand("game clear")
    public void gameClear(final OnlinePlayer player, final @Default("1") int amount) {
        if (lands.size() == 0) {
            player.sendMessage("There is none games to remove.");
            return;
        }

        System.out.println(String.format("Requested removal of %s but there's %s lands", amount, lands.size()));
        int maximumAmount = Math.min(amount, lands.size());
        for (int i = 0; i < maximumAmount; i++) {
            landReserver.clearSpot(lands.remove(0));
        }
        player.sendMessage(String.format("Cleared %s amount of games", maximumAmount));
    }

    @Subcommand("game size")
    @RequireLobbyToBeAvailable
    public void gameSize(final OnlinePlayer player) {
        player.sendMessage(String.format("There are %s amount of games available", lands.size()));
    }
}
