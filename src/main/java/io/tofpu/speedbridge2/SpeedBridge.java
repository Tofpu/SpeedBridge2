package io.tofpu.speedbridge2;

import io.tofpu.speedbridge2.command.CommandManager;
import io.tofpu.speedbridge2.command.subcommand.HelpCommandGenerator;
import io.tofpu.speedbridge2.database.manager.DatabaseManager;
import io.tofpu.speedbridge2.domain.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.service.IslandService;
import io.tofpu.speedbridge2.domain.service.PlayerService;
import io.tofpu.speedbridge2.listener.ListenerManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedBridge {
    private final JavaPlugin javaPlugin;
    private static BukkitAudiences adventure;

    public SpeedBridge(final JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void load() {
        adventure = BukkitAudiences.create(javaPlugin);

        DatabaseManager.load(javaPlugin).thenRun(() -> {
            final IslandService islandService = IslandService.INSTANCE;
            islandService.load();
            final PlayerService playerService = PlayerService.INSTANCE;
            playerService.load();
        });

        SchematicManager.INSTANCE.load(javaPlugin);
        ListenerManager.registerAll(javaPlugin);

        CommandManager.load(javaPlugin);
        HelpCommandGenerator.generateHelpCommand();
    }

    public void shutdown() {
        DatabaseManager.shutdown();
    }

    public static BukkitAudiences getAdventure() {
        if(adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }
}
