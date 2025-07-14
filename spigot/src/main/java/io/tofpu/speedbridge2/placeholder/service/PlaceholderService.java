package io.tofpu.speedbridge2.placeholder.service;

import io.tofpu.speedbridge2.placeholder.domain.InputStream;
import io.tofpu.speedbridge2.placeholder.domain.PlaceholderExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// %sb_game_timer%
// %sb_game_block_count%
// %sb_leaderboard_position%
// %sb_leaderboard_1_name%
public class PlaceholderService {
    private final Map<String, PlaceholderExpansion> expansions = new HashMap<>();

    public PlaceholderService() {
    }

//    public String handleOnPlaceholderRequest(Player player, String parms) {
//        if (parms == null || parms.isEmpty()) {
//            return "";
//        }
//
//        String[] parts = parms.split("_", 2);
//        if (parts.length < 2) {
//            return "";
//        }
//
//        String identifier = parts[0];
//        String input = parts[1];
//
//        PlaceholderExpansion expansion = expansions.get(identifier);
//        if (expansion != null) {
//            return expansion.onRequest(player, InputStream.from(input));
//        }
//        return "";
//    }

    public String handleOnPlaceholderRequest(Player player, String parms) {
        if (parms == null || parms.isEmpty()) {
            return null;
        }
        Optional<PlaceholderExpansion> expansion = expansion(parms);
        return expansion
                .map(placeholderExpansion -> placeholderExpansion.onRequest(player, InputStream.EMPTY))
                .orElse(null);
    }

    public void accept(PlaceholderVisitor visitor) {
        visitor.visit(this);
    }

    public void registerExpansion(PlaceholderExpansion ...expansions) {
        for (PlaceholderExpansion expansion : expansions) {
            this.expansions.put(expansion.identifier(), expansion);
        }
    }

    public Optional<PlaceholderExpansion> expansion(String identifier) {
        return Optional.ofNullable(expansions.get(identifier));
    }

    public String parse(Player player, String line) {
        return PlaceholderAPI.setPlaceholders(player, line);
    }
}
