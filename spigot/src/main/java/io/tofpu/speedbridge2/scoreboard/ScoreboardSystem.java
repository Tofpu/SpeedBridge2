package io.tofpu.speedbridge2.scoreboard;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.placeholder.service.PlaceholderService;
import io.tofpu.speedbridge2.reload.domain.ReloadRegistry;
import io.tofpu.speedbridge2.scoreboard.domain.Scoreboard;
import io.tofpu.speedbridge2.scoreboard.infra.config.ScoreboardConfigManager;
import io.tofpu.speedbridge2.scoreboard.infra.config.ScoreboardConfiguration;
import io.tofpu.speedbridge2.scoreboard.infra.listener.game.PlayerGameStateListener;
import io.tofpu.speedbridge2.scoreboard.service.ScoreboardService;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class ScoreboardSystem {
    private final File dataFolder;
    private final ScoreboardConfigManager configManager = new ScoreboardConfigManager();

    private ScoreboardLibrary scoreboardLibrary;
    private ScoreboardService scoreboardService;

    private BukkitTask updateTask;
    private ScoreboardConfiguration scoreboardConfiguration;

    public ScoreboardSystem(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public void initialize(Plugin plugin, PlaceholderService service) {
        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(plugin);
            plugin.getLogger().info("Scoreboard library initialized successfully.");
        } catch (NoPacketAdapterAvailableException e) {
            scoreboardLibrary = new NoopScoreboardLibrary();
            plugin.getLogger().warning("No scoreboard adapter found.");
        }

        loadScoreboardConfiguration();
        scoreboardService = new ScoreboardService(player -> createDynamicSidebar(service, player));

        int updateInterval = scoreboardConfiguration.tickInterval();
        updateTask = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> scoreboardService.updateAll(), 0L, updateInterval); // Update every second
    }

    private void loadScoreboardConfiguration() {
        scoreboardConfiguration = configManager.loadConfiguration(new File(dataFolder, "scoreboard.yml"));
    }

    private @NotNull Scoreboard createDynamicSidebar(PlaceholderService service, Player player) {
        Sidebar sidebar = scoreboardLibrary.createSidebar();

        int lineNumber = 0;
        SidebarComponent.Builder builder = SidebarComponent.builder();
        SidebarComponent titleComponent = SidebarComponent.blankLine();
        for (final String line : scoreboardConfiguration.lines()) {
            if (lineNumber == 0) {
                titleComponent = SidebarComponent.dynamicLine(() -> {
                    String updatedLine = service.parse(player, line);
                    return LegacyComponentSerializer.legacyAmpersand().deserialize(updatedLine);
                }); // Store the title separately
            } else {
                builder = builder.addDynamicLine(() -> {
                    String updatedLine = service.parse(player, line);
                    return LegacyComponentSerializer.legacyAmpersand().deserialize(updatedLine);
                });
            }
            lineNumber++;
        }

        ComponentSidebarLayout layout = new ComponentSidebarLayout(titleComponent, builder.build());

        layout.apply(sidebar);
        return new Scoreboard(sidebar, layout);
    }

    public void disable() {
        if (scoreboardLibrary != null) {
            scoreboardLibrary.close();
        }
        if (updateTask != null) {
            updateTask.cancel();
        }
    }

    public void registerListeners(EventBus eventBus) {
        ensureScoreboardServiceIsPresent();
        eventBus.register(new PlayerGameStateListener(scoreboardService));
    }

    public void registerReloadable(ReloadRegistry reloadRegistry) {
        ensureScoreboardServiceIsPresent();
        reloadRegistry.register(new ReloadableScoreboard(this));
    }

    public void reloadScoreboards() {
        ensureScoreboardServiceIsPresent();
        loadScoreboardConfiguration();

        // reconstruct the sidebars for all players for the new lines
        // to take effect immediately
        Collection<UUID> copy = new ArrayList<>(scoreboardService.players());
        scoreboardService.clearAll();

        for (UUID playerId : copy) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                scoreboardService.addPlayer(player);
            }
        }
    }

    private void ensureScoreboardServiceIsPresent() {
        if (scoreboardService == null) {
            throw new IllegalStateException("ScoreboardService is not initialized. Call initialize() first.");
        }
    }
}
