package io.tofpu.speedbridge2.scoreboard.service;

import io.tofpu.speedbridge2.scoreboard.domain.Scoreboard;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class ScoreboardService {
    private final Map<UUID, Scoreboard> sidebars = new HashMap<>();
    private final Function<Player, Scoreboard> sidebarFactory;

    public ScoreboardService(Function<Player, Scoreboard> sidebarFactory) {
        this.sidebarFactory = sidebarFactory;
    }

    public void updateAll() {
        for (Scoreboard scoreboard : sidebars.values()) {
            scoreboard.update();
        }
    }

    public void addPlayer(Player player) {
        Scoreboard sidebar = sidebars.get(player.getUniqueId());
        if (sidebar != null) return;
        sidebar = sidebarFactory.apply(player);
        sidebars.put(player.getUniqueId(), sidebar);
        sidebar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        Scoreboard sidebar = sidebars.remove(player.getUniqueId());
        if (sidebar != null) {
            sidebar.removePlayer(player);
            sidebar.close();
        }
    }
}
