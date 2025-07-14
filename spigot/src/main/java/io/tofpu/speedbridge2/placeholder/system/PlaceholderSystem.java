package io.tofpu.speedbridge2.placeholder.system;

import io.tofpu.speedbridge2.placeholder.service.PAPIPlaceholderExpansionHook;
import io.tofpu.speedbridge2.placeholder.service.PlaceholderService;
import org.bukkit.plugin.Plugin;

public class PlaceholderSystem {
    private final PlaceholderService placeholderService = new PlaceholderService();

    public void initialize(Plugin plugin) {
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            plugin.getLogger().info("PlaceholderAPI is enabled, registering placeholders.");
            new PAPIPlaceholderExpansionHook(placeholderService).register();
        } else {
            plugin.getLogger().warning("PlaceholderAPI is not enabled, placeholders will not be registered.");
        }
    }

    public PlaceholderService service() {
        return placeholderService;
    }
}
