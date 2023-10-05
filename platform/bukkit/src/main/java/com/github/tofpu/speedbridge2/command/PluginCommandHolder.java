package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.bridge.game.BridgeGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.Island;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.bridge.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.plugin.BukkitPlugin;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;


@Command({"speedbridge", "sb"})
public class PluginCommandHolder {
    private final LobbyService lobbyService;
    private final IslandSetupController islandSetupService;
    private final IslandService islandService;
    private final BridgeGameHandler gameHandler;

    public PluginCommandHolder(LobbyService lobbyService, IslandSetupController islandSetupService, IslandService islandService, BridgeGameHandler gameHandler) {
        this.lobbyService = lobbyService;
        this.islandSetupService = islandSetupService;
        this.islandService = islandService;
        this.gameHandler = gameHandler;
    }

    public PluginCommandHolder(BukkitPlugin bukkitPlugin) {
        this(bukkitPlugin.getService(LobbyService.class), bukkitPlugin.setupController(), bukkitPlugin.getService(IslandService.class), bukkitPlugin.gameHandler());
    }

    @Subcommand("lobby")
    public void lobby(final OnlinePlayer player) {
        if (!lobbyService.isLobbyAvailable()) {
            player.sendMessage("Lobby is not currently available!");
            return;
        }
        player.teleport(lobbyService.position());
    }

    @Subcommand("setlobby")
    public void setLobby(final OnlinePlayer player) {
        lobbyService.updateLocation(player.getPosition());
        player.sendMessage("Lobby location was successfully set.");
    }

    @Subcommand("game setup")
    @AutoComplete("* * schematics")
    public void gameSetup(final OnlinePlayer player, final int slot, final String schematicName) {
        if (!requireLobbyToBeAvailable(player)) return;
        if (islandSetupService.isInSetup(player.id())) {
            player.sendMessage("You are already in a setup!");
            return;
        }
        islandSetupService.begin(player, slot, schematicName);
    }

    @Subcommand("game setup cancel")
    public void gameSetupCancel(final OnlinePlayer player) {
        if (!requireLobbyToBeAvailable(player)) return;
        if (!islandSetupService.isInSetup(player.id())) {
            player.sendMessage("You are not in a setup!");
            return;
        }
        islandSetupService.cancel(player);
    }

    @Subcommand("game join")
    public void gameJoin(final OnlinePlayer player, final int slot) {
        if (!requireLobbyToBeAvailable(player)) return;
        Island island = islandService.get(slot);
        if (island == null) {
            player.sendMessage("Unknown island: " + slot);
            return;
        }
        gameHandler.start(player, island);
    }

    @Subcommand("game end")
    public void gameEnd(final OnlinePlayer player) {
        if (!requireLobbyToBeAvailable(player)) return;
        if (!gameHandler.isInGame(player.id())) {
            player.sendMessage("You are not playing!");
            return;
        }
        gameHandler.stop(player.id());
    }

    public boolean requireLobbyToBeAvailable(final OnlinePlayer player) {
        if (!lobbyService.isLobbyAvailable()) {
            player.sendMessage("Lobby is not currently available!");
            return false;
        }
        return true;
    }
}
