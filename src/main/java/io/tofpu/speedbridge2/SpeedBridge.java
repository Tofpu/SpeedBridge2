package io.tofpu.speedbridge2;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.command.CommandManager;
import io.tofpu.speedbridge2.command.subcommand.HelpCommandGenerator;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.DatabaseManager;
import io.tofpu.speedbridge2.domain.extra.blockmenu.BlockMenuManager;
import io.tofpu.speedbridge2.domain.extra.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.island.object.IslandBoard;
import io.tofpu.speedbridge2.domain.island.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.island.setup.IslandSetupManager;
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

        log("Loading the `config.yml`...");
        ConfigurationManager.INSTANCE.load(javaPlugin);
        ExpansionHandler.INSTANCE.load();

        try {
            DynamicClass.addParameters(javaPlugin);
            DynamicClass.alternativeScan(getClass().getClassLoader(), "io.tofpu" +
                    ".speedbridge2");
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            log("Hooking into PlaceholderAPI...");
            new PluginExpansion(javaPlugin);
        }

        log("Loading the `speedbridge2` world...");
        SchematicManager.INSTANCE.load(javaPlugin);

        IslandSetupManager.INSTANCE.load();

        log("Registering the commands...");
        CommandManager.load(javaPlugin);

        log("Loading the Block Menu GUI.");
        BlockMenuManager.INSTANCE.load();

        log("Loading the database...");
        DatabaseManager.load(javaPlugin).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throw new IllegalStateException(throwable);
            }

            final IslandService islandService = IslandService.INSTANCE;
            log("Loading the island data...");
            islandService.load().whenComplete((integerIslandMap, throwable1) -> {
                if (throwable1 != null) {
                    throw new IllegalStateException(throwable1);
                }

                log("Loading the global/session leaderboard...");
                Leaderboard.INSTANCE.load(javaPlugin)
                        .whenComplete((unused1, throwable2) -> {
                            if (throwable2 != null) {
                                throw new IllegalStateException(throwable2);
                            }

                            log("Loading the island leaderboard...");
                            // when the global leaderboard is complete, load the per-island
                            // leaderboard
                            IslandBoard.load(javaPlugin).whenComplete((o, throwable3) -> {
                                if (throwable3 != null) {
                                    throw new IllegalStateException(throwable3);
                                }

                                // once the database is loaded once, and for all - load
                                // the players that are currently online
                                for (final Player player : Bukkit.getOnlinePlayers()) {
                                    PlayerService.INSTANCE.internalRefresh(player);
                                }

                                log("Complete.");
                            });
                        });
            });

            log("Loading the messages...");
            Message.load(javaPlugin.getDataFolder());
        });

        log("Generating `/sb help` message...");
        HelpCommandGenerator.generateHelpCommand(javaPlugin);
    }

    public void shutdown() {
        log("Shutting down the database...");
        DatabaseManager.shutdown();
        PluginExecutor.INSTANCE.shutdown();

        log("Doing clean-up operations...");
        PlayerService.INSTANCE.shutdown();

        log("Unloading the `speedbridge2` world...");
        Bukkit.unloadWorld("speedbridge2", false);

        log("Complete.");
    }

    private void log(final String content) {
        javaPlugin.getLogger().info(content);
    }

    public static BukkitAudiences getAdventure() {
        if(adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }
}
