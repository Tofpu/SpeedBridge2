package io.tofpu.speedbridge2.scoreboard;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.scoreboard.infra.config.ScoreboardConfigManager;
import io.tofpu.speedbridge2.scoreboard.infra.config.ScoreboardConfiguration;
import io.tofpu.speedbridge2.scoreboard.infra.listener.game.PlayerGameStateListener;
import io.tofpu.speedbridge2.scoreboard.service.ScoreboardService;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ScoreboardSystem {
    private ScoreboardLibrary scoreboardLibrary;
    private ScoreboardService scoreboardService;

    public void initialize(Plugin plugin) {
        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(plugin);
            plugin.getLogger().info("Scoreboard library initialized successfully.");
        } catch (NoPacketAdapterAvailableException e) {
            scoreboardLibrary = new NoopScoreboardLibrary();
            plugin.getLogger().warning("No scoreboard adapter found.");
        }

        ScoreboardConfigManager configManager = new ScoreboardConfigManager();
        ScoreboardConfiguration scoreboardConfiguration = configManager.loadConfiguration(new File(plugin.getDataFolder(), "scoreboard.yml"));

        Sidebar sidebar = scoreboardLibrary.createSidebar();

        int lineNumber = 0;
        for (String line : scoreboardConfiguration.lines()) {
            TextComponent component = LegacyComponentSerializer.legacyAmpersand().deserialize(line);
            if (lineNumber == 0) {
                sidebar.title(component);
            } else {
                sidebar.line(lineNumber - 1, component);
            }
            lineNumber++;
        }

        scoreboardService = new ScoreboardService(sidebar);
    }

    public void disable() {
        if (scoreboardLibrary != null) {
            scoreboardLibrary.close();
        }
    }

    public void registerListeners(EventBus eventBus) {
        if (scoreboardService == null) {
            throw new IllegalStateException("ScoreboardService is not initialized. Call initialize() first.");
        }
        eventBus.register(new PlayerGameStateListener(scoreboardService));
    }
}
