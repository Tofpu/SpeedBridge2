package io.tofpu.speedbridge2;

import io.tofpu.dynamicclass.DynamicClass;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.command.CommandManager;
import io.tofpu.speedbridge2.command.subcommand.HelpCommandGenerator;
import io.tofpu.speedbridge2.model.blockmenu.BlockMenuManager;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.PluginExecutor;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.database.DatabaseManager;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.common.util.UpdateChecker;
import io.tofpu.speedbridge2.model.island.IslandFactory;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import io.tofpu.speedbridge2.model.island.object.setup.IslandSetupFactory;
import io.tofpu.speedbridge2.model.island.object.setup.IslandSetupHandler;
import io.tofpu.speedbridge2.model.leaderboard.IslandBoard;
import io.tofpu.speedbridge2.model.leaderboard.Leaderboard;
import io.tofpu.speedbridge2.model.player.PlayerFactory;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.support.placeholderapi.PluginExpansion;
import io.tofpu.speedbridge2.model.support.placeholderapi.expansion.ExpansionHandler;
import io.tofpu.umbrella.UmbrellaAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public final class SpeedBridge {
    private static BukkitAudiences adventure;

    private final JavaPlugin javaPlugin;
    private final IslandService islandService;
    private final PlayerService playerService;
    private final ArenaManager arenaManager;
    private final Leaderboard leaderboard;

    public SpeedBridge(final JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.islandService = new IslandService();
        this.playerService = new PlayerService();
        this.arenaManager = new ArenaManager();
        this.leaderboard = new Leaderboard(playerService);
    }

    public void load() {
        // reset the world, in-case it does exist
        arenaManager.resetWorld();
    }

    public void enable() {
        adventure = BukkitAudiences.create(javaPlugin);

        new UmbrellaAPI(javaPlugin)
                .enable();

        new Metrics(javaPlugin, 14597);

        MultiWorldEditAPI.load(javaPlugin);

        log("Loading the `config.conf` & 'items.yml'...");
        ConfigurationManager.INSTANCE.load(javaPlugin);

        try {
            DynamicClass.addParameters(javaPlugin, playerService, islandService,
                    arenaManager, leaderboard);
            DynamicClass.alternativeScan(getClass().getClassLoader(), "io.tofpu" +
                    ".speedbridge2");

            Field objectMap = DynamicClass.class.getDeclaredField("OBJECT_MAP");
            objectMap.setAccessible(true);
            Map<Class<?>, Object> o = (Map<Class<?>, Object>) objectMap.get(null);
            BridgeUtil.debug("key set of dynamic class: " + o.keySet());
        } catch (final IOException | NoClassDefFoundError | NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            ExpansionHandler.INSTANCE.load();
            
            log("Hooking into PlaceholderAPI...");
            new PluginExpansion(javaPlugin, playerService);
        }

        log("Loading the `speedbridge2` world...");
        arenaManager.load();

        IslandSetupHandler.INSTANCE.load();

        log("Registering the commands...");
        CommandManager.load(javaPlugin, playerService, islandService, arenaManager);

        log("Loading the Block Menu GUI.");
        BlockMenuManager.INSTANCE.load();

        IslandFactory.init(islandService, arenaManager);
        IslandSetupFactory.init(islandService);
        PlayerFactory.init(islandService, leaderboard);
        this.islandService.init(arenaManager);

        log("Loading the database...");
        BridgeUtil.whenComplete(DatabaseManager.loadAsync(javaPlugin), () -> {
            log("Loading the island data...");
            BridgeUtil.whenComplete(islandService.loadAsync(), () -> {

                log("Loading the global/session leaderboard...");
                BridgeUtil.whenComplete(leaderboard.loadAsync(), () -> {

                    log("Loading the island leaderboard...");
                    // when the global leaderboard is complete, load the per-island
                    // leaderboard
                    BridgeUtil.whenComplete(IslandBoard.loadAsync(javaPlugin), () -> {

                        // once the database is loaded once, and for all - load
                        // the players that are currently online
                        for (final Player player : Bukkit.getOnlinePlayers()) {
                            playerService.loadAsync(player.getUniqueId());
                        }

                        log("The database has been fully loaded.");
                    });
                });
            });
        });

        log("Loading the messages...");
        Message.load(javaPlugin.getDataFolder());

        log("Generating `/sb help` message...");
        HelpCommandGenerator.generateHelpCommand(javaPlugin);

        log("Checking for an update...");
        UpdateChecker.init(javaPlugin, 100619)
                .requestUpdateCheck()
                .whenComplete((updateResult, throwable) -> {
                    if (throwable != null) {
                        log("Couldn't check for an update...");
                        return;
                    }

                    if (updateResult.requiresUpdate()) {
                        log("You're using an outdated version of SpeedBridge!");
                        log("You can download the latest version at https://www.spigotmc.org/resources/.100619/");
                    } else {
                        log("You're using the latest version!");
                    }
                });
    }

    public void shutdown() {
        log("Shutting down the database...");
        DatabaseManager.shutdown();
        PluginExecutor.INSTANCE.shutdown();

        log("Doing clean-up operations...");
        playerService.shutdown();

        log("Unloading the `speedbridge2` world...");
        arenaManager.unloadWorld();
        arenaManager.resetWorld();

        final UmbrellaAPI umbrellaAPI = UmbrellaAPI.getInstance();
        if (umbrellaAPI != null) {
            umbrellaAPI.disable();
        }

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
