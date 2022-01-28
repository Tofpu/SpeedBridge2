package io.tofpu.speedbridge2.support.placeholderapi;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.misc.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class PluginExpansion extends PlaceholderExpansion {
    private final Plugin plugin;
    private final PluginDescriptionFile descriptionFile;

    private final IslandService islandService = IslandService.INSTANCE;
    private final PlayerService playerService = PlayerService.INSTANCE;

    public PluginExpansion(final Plugin plugin) {
        this.plugin = plugin;
        this.descriptionFile = this.plugin.getDescription();

        register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "speedbridge";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.valueOf(this.descriptionFile.getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return this.descriptionFile.getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(final Player player,
            @NotNull final String params) {
        if (player == null) {
            return "";
        }

        final BridgePlayer bridgePlayer = playerService.get(player.getUniqueId());
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();
        switch (params) {
            case "island_slot":
                if (gamePlayer == null) {
                    return "";
                }
                return gamePlayer.getCurrentGame().getIsland().getSlot() + "";
            case "best_score":
                Score bestScore = null;
                for (final Score score : bridgePlayer.getScores()) {
                    if (bestScore != null && score.compareTo(bestScore) >= 1) {
                        continue;
                    }
                    bestScore = score;
                }

                if (bestScore == null) {
                    return Message.INSTANCE.EMPTY_SCORE_FORMAT;
                }

                return BridgeUtil.toFormattedScore(bestScore.getScore());
            case "timer":
                if (gamePlayer == null) {
                    return "";
                }
                return (System.nanoTime() - gamePlayer.getTimer() + "");
            case "total_wins":
                // TODO:
                return "";
            case "total_tries":
                // TODO:
                return "";
        }

        return "";
    }
}
