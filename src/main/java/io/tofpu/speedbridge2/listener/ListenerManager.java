package io.tofpu.speedbridge2.listener;

import io.tofpu.speedbridge2.listener.game.IslandInteractionListener;
import io.tofpu.speedbridge2.listener.island.IslandProtectionListener;
import io.tofpu.speedbridge2.listener.island.IslandRegionListener;
import io.tofpu.speedbridge2.listener.island.IslandResetListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public final class ListenerManager {
    private static final List<Listener> LISTENERS = new ArrayList<>();

    static {
        new IslandRegionListener();
        new IslandProtectionListener();
        new IslandResetListener();

        new IslandInteractionListener();
    }

    public static void add(final Listener listener) {
        LISTENERS.add(listener);
    }

    public static void registerAll(final Plugin plugin) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        for (final Listener listener : LISTENERS) {
            pluginManager.registerEvents(listener, plugin);
        }
    }
}
