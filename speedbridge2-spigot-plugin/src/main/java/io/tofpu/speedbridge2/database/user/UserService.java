package io.tofpu.speedbridge2.database.user;

import io.tofpu.speedbridge2.database.user.repository.name.AbstractUserNameRepository;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This is where the business logic of {@link AbstractUserNameRepository}
 * resides.
 */
public interface UserService {
    CompletableFuture<Void> init();

    CompletableFuture<BridgePlayer> fetch(UUID key);

    CompletableFuture<Boolean> isPresent(UUID key);

    CompletableFuture<Void> insert(UUID key, BridgePlayer obj);

    CompletableFuture<Void> delete(UUID key);
}
