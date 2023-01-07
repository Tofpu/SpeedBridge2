package io.tofpu.speedbridge2.database.repository.user.name;

import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.repository.Repository;
import io.tofpu.speedbridge2.repository.TableBaseRepository;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

import java.util.UUID;



/**
 * A {@link Repository} abstract class for {@link BridgePlayer}'s name.
 */
public abstract class AbstractUserNameRepository extends TableBaseRepository<UUID, String> {
    protected AbstractUserNameRepository(final BaseStorage storage) {
        super(storage);
    }
}
