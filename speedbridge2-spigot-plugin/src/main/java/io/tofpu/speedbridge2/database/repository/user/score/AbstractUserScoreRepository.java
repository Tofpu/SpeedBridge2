package io.tofpu.speedbridge2.database.repository.user.score;

import io.tofpu.speedbridge2.database.repository.user.score.key.ScoreUUID;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.repository.Repository;
import io.tofpu.speedbridge2.repository.TableBaseRepository;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

/**
 * A {@link Repository} abstract class for {@link BridgePlayer}'s {@link Score}.
 */
public abstract class AbstractUserScoreRepository extends TableBaseRepository<ScoreUUID, Score> {
    protected AbstractUserScoreRepository(final BaseStorage storage) {
        super(storage);
    }
}
