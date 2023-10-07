package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.BukkitMessages;
import com.github.tofpu.speedbridge2.bridge.game.Island;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.bridge.score.Score;
import com.github.tofpu.speedbridge2.bridge.score.Scores;
import com.github.tofpu.speedbridge2.bridge.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.plugin.BukkitPlugin;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;


@Command({"speedbridge", "sb"})
public class PluginCommandHolder {
    private final LobbyService lobbyService;
    private final IslandSetupController islandSetupService;
    private final IslandService islandService;
    private final IslandGameHandler gameHandler;
    private final BridgeScoreService scoreService;

    public PluginCommandHolder(LobbyService lobbyService, IslandSetupController islandSetupService, IslandService islandService, IslandGameHandler gameHandler, BridgeScoreService scoreService) {
        this.lobbyService = lobbyService;
        this.islandSetupService = islandSetupService;
        this.islandService = islandService;
        this.gameHandler = gameHandler;
        this.scoreService = scoreService;
    }

    public PluginCommandHolder(BukkitPlugin bukkitPlugin) {
        this(bukkitPlugin.getService(LobbyService.class), bukkitPlugin.setupController(), bukkitPlugin.getService(IslandService.class), bukkitPlugin.gameHandler(), bukkitPlugin.getService(BridgeScoreService.class));
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

    @Subcommand("score")
    public void score(final OnlinePlayer player, @Optional Integer islandSlot) {
        Score bestScore;
        if (islandSlot == null) {
            bestScore = scoreService.getBestScore(player.id());
        } else bestScore = scoreService.getBestScore(player.id(), islandSlot);

        if (bestScore == null) {
            player.sendMessage(BukkitMessages.NO_PERSONAL_BEST);
            return;
        }

        player.sendMessage(BukkitMessages.PERSONAL_BEST, bestScore.seconds(), bestScore.getIslandSlot());
    }

    public boolean requireLobbyToBeAvailable(final OnlinePlayer player) {
        if (!lobbyService.isLobbyAvailable()) {
            player.sendMessage("Lobby is not currently available!");
            return false;
        }
        return true;
    }
}
