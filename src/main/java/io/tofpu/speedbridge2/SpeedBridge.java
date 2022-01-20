package io.tofpu.speedbridge2;

import io.tofpu.speedbridge2.command.CommandManager;
import io.tofpu.speedbridge2.command.PluginCommand;
import io.tofpu.speedbridge2.database.manager.DatabaseManager;
import io.tofpu.speedbridge2.domain.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.service.IslandService;
import io.tofpu.speedbridge2.domain.service.PlayerService;
import io.tofpu.speedbridge2.listener.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedBridge {
    private final JavaPlugin javaPlugin;

    public SpeedBridge(final JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void load() {
        DatabaseManager.load(javaPlugin).thenRun(() -> {
            final IslandService islandService = IslandService.INSTANCE;
            islandService.load();
            final PlayerService playerService = PlayerService.INSTANCE;
            playerService.load();
        });

        SchematicManager.INSTANCE.load(javaPlugin);
        ListenerManager.registerAll(javaPlugin);

        CommandManager.load(javaPlugin);

        javaPlugin.getCommand("bridge").setExecutor(new PluginCommand());
    }

    public void shutdown() {
        DatabaseManager.shutdown();
    }
}
