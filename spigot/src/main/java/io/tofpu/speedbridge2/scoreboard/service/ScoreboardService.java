package io.tofpu.speedbridge2.scoreboard.service;

import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import org.bukkit.entity.Player;

public class ScoreboardService {
    private final Sidebar sidebar;

    public ScoreboardService(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    public void addPlayer(Player player) {
        sidebar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        sidebar.removePlayer(player);
    }
}
