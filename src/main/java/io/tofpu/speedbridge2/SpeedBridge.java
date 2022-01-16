package io.tofpu.speedbridge2;

import io.tofpu.speedbridge2.command.PluginCommand;
import io.tofpu.speedbridge2.database.manager.DatabaseManager;
import io.tofpu.speedbridge2.domain.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.service.IslandService;
import org.bukkit.plugin.java.JavaPlugin;

public class SpeedBridge {
    private final JavaPlugin javaPlugin;

    public SpeedBridge(final JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void load() {
        DatabaseManager.load(javaPlugin).thenRun(() -> {
            final IslandService service = IslandService.INSTANCE;
            service.load();
        });

        SchematicManager.INSTANCE.load(javaPlugin);

        javaPlugin.getCommand("bridge").setExecutor(new PluginCommand());
    }

    public void shutdown() {
        DatabaseManager.shutdown();
    }
}
