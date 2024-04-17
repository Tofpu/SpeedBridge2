package io.tofpu.speedbridge2.model.support.placeholderapi;

import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.support.placeholderapi.expansion.ExpansionHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public final class PluginExpansion extends PlaceholderExpansion {
    private final Plugin plugin;
    private final PluginDescriptionFile descriptionFile;

    private final PlayerService playerService;

    public PluginExpansion(final Plugin plugin, final PlayerService playerService) {
        this.plugin = plugin;
        this.playerService = playerService;
        this.descriptionFile = this.plugin.getDescription();

        register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sb";
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

        final BridgePlayer bridgePlayer = playerService.getIfPresent(player.getUniqueId());
        if (bridgePlayer == null) {
            return "";
        }

        final String[] args = params.split("_");
        return ExpansionHandler.INSTANCE.match(bridgePlayer, args);
    }
}
