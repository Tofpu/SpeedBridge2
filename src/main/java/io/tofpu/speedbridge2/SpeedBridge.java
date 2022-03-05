package io.tofpu.speedbridge2;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.command.CommandManager;
import io.tofpu.speedbridge2.command.subcommand.HelpCommandGenerator;
import io.tofpu.speedbridge2.domain.blockmenu.BlockMenuManager;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.DatabaseManager;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.island.object.IslandBoard;
import io.tofpu.speedbridge2.domain.island.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.island.setup.IslandSetupManager;
import io.tofpu.speedbridge2.domain.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.support.placeholderapi.PluginExpansion;
import io.tofpu.speedbridge2.support.placeholderapi.expansion.ExpansionHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SpeedBridge {
    private final JavaPlugin javaPlugin;
    private static BukkitAudiences adventure;

    public SpeedBridge(final JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void load() {
        adventure = BukkitAudiences.create(javaPlugin);

        MultiWorldEditAPI.load(javaPlugin);

        ConfigurationManager.INSTANCE.load(javaPlugin);
        ExpansionHandler.INSTANCE.load();

        try {
            DynamicClass.addParameters(javaPlugin);
            DynamicClass.alternativeScan(getClass().getClassLoader(), "io.tofpu" +
                    ".speedbridge2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PluginExpansion(javaPlugin);
        }

        SchematicManager.INSTANCE.load(javaPlugin);
        IslandSetupManager.INSTANCE.load();
        CommandManager.load(javaPlugin);
        BlockMenuManager.INSTANCE.load();

        DatabaseManager.load(javaPlugin).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throw new IllegalStateException(throwable);
            }

            final IslandService islandService = IslandService.INSTANCE;
            islandService.load();

            Leaderboard.INSTANCE.load(javaPlugin)
                    .whenComplete((unused1, throwable1) -> {
                        if (throwable1 != null) {
                            throw new IllegalStateException(throwable1);
                        }

                        // when the global leaderboard is complete, load the per-island
                        // leaderboard
                        IslandBoard.load(javaPlugin);

                        // once the database is loaded once, and for all - load
                        // the players that are currently online
                        for (final Player player : Bukkit.getOnlinePlayers()) {
                            PlayerService.INSTANCE.internalRefresh(player);
                        }
                    });

            Message.load(javaPlugin.getDataFolder());
        });

        HelpCommandGenerator.generateHelpCommand(javaPlugin);
    }

    public void shutdown() {
        DatabaseManager.shutdown();
        PluginExecutor.INSTANCE.shutdown();
    }

    public static BukkitAudiences getAdventure() {
        if(adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }
}
