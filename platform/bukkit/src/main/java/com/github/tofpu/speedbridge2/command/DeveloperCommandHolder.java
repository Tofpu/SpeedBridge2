package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.bridge.IslandSchematic;
import com.github.tofpu.speedbridge2.bridge.core.arena.ArenaManager;
import com.github.tofpu.speedbridge2.island.Island;
import com.github.tofpu.speedbridge2.bridge.Land;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.plugin.BukkitPlugin;
import com.github.tofpu.speedbridge2.schematic.Schematic;
import com.github.tofpu.speedbridge2.schematic.SchematicHandler;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;

@Command({"speedbridge dev", "sb dev"})
@CommandPermission("sb.dev")
public class DeveloperCommandHolder {
    private final LobbyService lobbyService;
    private final IslandService islandService;
    private final ArenaManager arenaManager;
    private final SchematicHandler schematicHandler;
    private final ArenaAdapter arenaAdapter;

    private final List<Land> lands = new ArrayList<>();

    public DeveloperCommandHolder(BukkitPlugin plugin) {
        this(plugin.getService(LobbyService.class), plugin.getService(IslandService.class), plugin.gameHandler().landController().arenaManager(), plugin.schematicHandler(), plugin.arenaAdapter());
    }

    public DeveloperCommandHolder(LobbyService lobbyService, IslandService islandService, ArenaManager arenaManager, SchematicHandler schematicHandler, ArenaAdapter arenaAdapter) {
        this.lobbyService = lobbyService;
        this.islandService = islandService;
        this.arenaManager = arenaManager;
        this.schematicHandler = schematicHandler;
        this.arenaAdapter = arenaAdapter;
    }

    @Subcommand("game create")
    public void gameCreate(final OnlinePlayer player, final int slot, final @Default("1") int amount) {
        if (!requireLobbyToBeAvailable(player)) return;
        Island island = islandService.get(slot);
        if (island == null) {
            player.sendMessage("Unknown island: " + slot);
            return;
        }

        for (int i = 0; i < amount; i++) {
            Schematic schematic = schematicHandler.getSafe(island.getSchematicName());
            IslandSchematic islandSchematic = new IslandSchematic(island.getSlot(), schematic, arenaAdapter.gameWorld());
            lands.add(arenaManager.generate(islandSchematic, arenaAdapter.gameWorld()));
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
            arenaManager.unlock(lands.remove(0));
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
            arenaManager.clear(lands.remove(0));
        }
        player.sendMessage(String.format("Cleared %s amount of games", maximumAmount));
    }

    @Subcommand("game size")
    public void gameSize(final OnlinePlayer player) {
        if (!requireLobbyToBeAvailable(player)) return;
        player.sendMessage(String.format("There are %s amount of games available", lands.size()));
    }

    private boolean requireLobbyToBeAvailable(final OnlinePlayer player) {
        if (!lobbyService.isLobbyAvailable()) {
            player.sendMessage("Lobby is not currently available!");
            return false;
        }
        return true;
    }
}
