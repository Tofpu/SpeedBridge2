package io.tofpu.speedbridge2.database.user.repository;

import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.repository.Repository;
import io.tofpu.speedbridge2.repository.TableBaseRepository;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

import java.util.UUID;

/**
 * A {@link Repository} abstract class for {@link BridgePlayer}.
 */
public abstract class AbstractUserRepository extends TableBaseRepository<UUID, BridgePlayer> {
    protected AbstractUserRepository(final BaseStorage storage) {
        super(storage);
    }
}
