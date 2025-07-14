package io.tofpu.speedbridge2.placeholder.service;

import io.tofpu.speedbridge2.SpeedbridgePlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.tofpu.speedbridge2.Constants.Placeholder.PLACEHOLDER_PREFIX;

public class PAPIPlaceholderExpansionHook extends PlaceholderExpansion {
    private final PlaceholderService placeholderService;

    public PAPIPlaceholderExpansionHook(PlaceholderService placeholderService) {
        this.placeholderService = placeholderService;
    }

    @Override
    public @NotNull String getIdentifier() {
        return PLACEHOLDER_PREFIX;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", pluginDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return pluginDescription().getVersion();
    }

    @SuppressWarnings("deprecation")
    private static @NotNull PluginDescriptionFile pluginDescription() {
        return SpeedbridgePlugin.getPlugin(SpeedbridgePlugin.class).getDescription();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
//        System.out.println("PAPI Placeholder Request: " + params);
        return placeholderService.handleOnPlaceholderRequest(player, params);
    }
}
