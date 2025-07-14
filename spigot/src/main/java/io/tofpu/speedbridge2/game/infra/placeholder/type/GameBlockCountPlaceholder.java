package io.tofpu.speedbridge2.game.infra.placeholder.type;

import io.tofpu.speedbridge2.game.service.GameService;
import io.tofpu.speedbridge2.placeholder.domain.InputStream;
import io.tofpu.speedbridge2.placeholder.domain.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class GameBlockCountPlaceholder extends PlaceholderExpansion {
    private final GameService gameService;

    public GameBlockCountPlaceholder(GameService gameService) {
        super("block_count");
        this.gameService = gameService;
    }

    @Override
    public String onRequest(Player player, InputStream inputStream) {
        return gameService.game(player.getUniqueId())
                .map(game -> String.valueOf(game.gamePlayer().placedBlocks().size()))
                .orElse(""); // If the player is not in a game, return an empty string
    }
}
