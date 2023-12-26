package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.BukkitMessages;
import com.github.tofpu.speedbridge2.bridge.game.Island;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.bridge.score.Score;
import com.github.tofpu.speedbridge2.bridge.setup.IslandSetupController;
import com.github.tofpu.speedbridge2.configuration.message.ConfigurableMessageService;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.plugin.BukkitPlugin;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;

import java.util.Map;


@Command({"speedbridge", "sb"})
public class PluginCommandHolder {
    private final LobbyService lobbyService;
    private final IslandSetupController islandSetupService;
    private final IslandService islandService;
    private final IslandGameHandler gameHandler;
    private final BridgeScoreService scoreService;
    private final ConfigurableMessageService configurableMessageService;

    public PluginCommandHolder(LobbyService lobbyService, IslandSetupController islandSetupService, IslandService islandService, IslandGameHandler gameHandler, BridgeScoreService scoreService, ConfigurableMessageService configurableMessageService) {
        this.lobbyService = lobbyService;
        this.islandSetupService = islandSetupService;
        this.islandService = islandService;
        this.gameHandler = gameHandler;
        this.scoreService = scoreService;
        this.configurableMessageService = configurableMessageService;
    }

    public PluginCommandHolder(BukkitPlugin bukkitPlugin) {
        this(bukkitPlugin.getService(LobbyService.class), bukkitPlugin.setupController(), bukkitPlugin.getService(IslandService.class), bukkitPlugin.gameHandler(), bukkitPlugin.getService(BridgeScoreService.class), bukkitPlugin.getService(ConfigurableMessageService.class));
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

    @Subcommand("game setup create")
    @AutoComplete("* schematics")
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
        String message;
        if (islandSlot == null) {
            message = bestScoresOnAllIslands(player);
        } else {
            message = specificBestScore(player, islandSlot);
        }

        if (message == null || message.isEmpty()) {
            player.sendMessage(BukkitMessages.NO_PERSONAL_BEST);
            return;
        }

        player.sendMessage(message);
    }

    private String specificBestScore(OnlinePlayer player, Integer islandSlot) {
        Score bestScore = scoreService.getBestScore(player.id(), islandSlot);
        if (bestScore == null) {
            return BukkitMessages.NO_PERSONAL_BEST_ON_ISLAND.defaultMessage(islandSlot);
        }
        return BukkitMessages.PERSONAL_BEST.defaultMessage(bestScore.textSeconds(), bestScore.getIslandSlot());
    }

    private String bestScoresOnAllIslands(OnlinePlayer player) {
        Map<Integer, Score> bestScores = scoreService.getBestScoresFromAllIslands(player.id());
        if (bestScores.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(BukkitMessages.PERSONAL_BEST_GLOBAL_TITLE.defaultMessage())
                .append("\n");

        bestScores.forEach((slot, score) -> {
            builder.append(BukkitMessages.PERSONAL_BEST_GLOBAL_BODY.defaultMessage(score.textSeconds(), slot))
                    .append("\n");
        });

        return builder.toString();
    }

    @Subcommand("reload")
    public void reload(final OnlinePlayer player) {
        try {
            configurableMessageService.reload();
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(BukkitMessages.RELOAD_ERROR);
            return;
        }
        player.sendMessage(BukkitMessages.SUCCESSFUL_RELOAD);
    }

    public boolean requireLobbyToBeAvailable(final OnlinePlayer player) {
        if (!lobbyService.isLobbyAvailable()) {
            player.sendMessage("Lobby is not currently available!");
            return false;
        }
        return true;
    }
}
