package io.tofpu.speedbridge2.database.user.repository.block;

import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.repository.Repository;
import io.tofpu.speedbridge2.repository.TableBaseRepository;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

import java.util.UUID;

/**
 * A {@link Repository} abstract class for {@link BridgePlayer}'s block choice.
 */
public abstract class AbstractUserBlockChoiceRepository extends TableBaseRepository<UUID, String> {
    protected AbstractUserBlockChoiceRepository(final BaseStorage storage) {
        super(storage);
    }
}
