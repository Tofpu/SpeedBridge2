package io.tofpu.speedbridge2.service.user;

import io.tofpu.speedbridge2.model.player.object.BridgePlayer;

import java.util.concurrent.CompletableFuture;

/**
 * This is where the business logic of {@link BridgePlayer}
 * resides.
 */
public interface UserService {
    CompletableFuture<Void> init();
}
