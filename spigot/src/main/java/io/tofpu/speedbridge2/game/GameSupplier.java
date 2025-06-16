package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.game.domain.Game;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * A class that provides a game that is currently running for a player.
 */
public interface GameSupplier {
    /**
     * Calls the consumer if a game is present for the player.
     *
     * @param playerId the player id
     * @param consumer the consumer that will be called if a game is present
     */
    void ifGamePresent(UUID playerId, Consumer<Game> consumer);
}
