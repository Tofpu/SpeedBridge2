package io.tofpu.speedbridge2.placeholder.domain;

import org.bukkit.entity.Player;

public abstract class PlaceholderExpansion {
    private final String identifier;

    public PlaceholderExpansion(String identifier) {
        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    public abstract String onRequest(Player player, InputStream inputStream);
}
