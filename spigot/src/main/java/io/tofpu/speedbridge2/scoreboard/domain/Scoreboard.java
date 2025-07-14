package io.tofpu.speedbridge2.scoreboard.domain;

import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import org.bukkit.entity.Player;

public record Scoreboard(
        Sidebar sidebar,
        ComponentSidebarLayout layout
) {
    public void addPlayer(Player player) {
        sidebar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        sidebar.removePlayer(player);
    }

    public void update() {
        layout.apply(sidebar);
    }

    public void close() {
        sidebar.close();
    }
}
