package com.github.tofpu.speedbridge2.bukkit.command;

import com.github.tofpu.speedbridge2.bukkit.BukkitMessages;
import com.github.tofpu.speedbridge2.bukkit.plugin.BukkitPlugin;
import com.github.tofpu.speedbridge2.bukkit.util.MessageBuilder;
import com.github.tofpu.speedbridge2.common.game.BridgeSystem;
import com.github.tofpu.speedbridge2.common.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.common.game.score.object.Score;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import com.github.tofpu.speedbridge2.configuration.message.ConfigurableMessageService;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.help.CommandHelp;

import java.util.Map;


@Command({"speedbridge", "sb"})
public class PluginCommandHolder {
    private final LobbyService lobbyService;
    private final BridgeScoreService scoreService;
    private final ConfigurableMessageService configurableMessageService;

    public PluginCommandHolder(LobbyService lobbyService, BridgeScoreService scoreService, ConfigurableMessageService configurableMessageService) {
        this.lobbyService = lobbyService;
        this.scoreService = scoreService;
        this.configurableMessageService = configurableMessageService;
    }

    public PluginCommandHolder(BukkitPlugin bukkitPlugin) {
        this(bukkitPlugin.getService(LobbyService.class), bukkitPlugin.getService(BridgeScoreService.class), bukkitPlugin.getService(ConfigurableMessageService.class));
    }

    @Subcommand("lobby")
    public void lobby(final OnlinePlayer player) {
        if (!lobbyService.isLobbyAvailable()) {
            player.sendMessage(BukkitMessages.LOBBY_NOT_AVAILABLE);
            return;
        }
        player.teleport(lobbyService.position());
    }

    @Subcommand("setlobby")
    public void setLobby(final OnlinePlayer player) {
        lobbyService.updateLocation(player.getPosition());
        player.sendMessage(BukkitMessages.LOBBY_SET);
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

    @Subcommand("help")
    public void help(final OnlinePlayer player, final CommandHelp<String> helpEntries, final @Default("1") int page) {
        MessageBuilder messageBuilder = new MessageBuilder()
                .appendNewLine()
                .append("&8&l|>&r &e&lSpeedBridge3 &fv2.0-dev").appendNewLine()
                .appendNewLine()
                .append("&8&l|- &eGeneral Info:").appendNewLine()
                .append("&8|-> &7Author:").appendSpace().append("&fTofpu").appendNewLine()
                .append("&8|-> &7Discord:").appendSpace().append("&fhttps://discord.gg/cNwykvym2x").appendNewLine()
                .appendNewLine()
                .append("&8&l|- &eCommands:").appendSpace().append("&7[" + page + " out of " + helpEntries.getPageSize(7) + " pages]").appendNewLine();

        for (String entry : helpEntries.paginate(page, 7)) {
            messageBuilder.append(entry).appendNewLine();
        }

        player.sendMessage(messageBuilder.toString());
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
}
