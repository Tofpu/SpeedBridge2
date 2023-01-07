package io.tofpu.speedbridge2.database.repository.user.factory;

import io.tofpu.speedbridge2.database.repository.user.block.AbstractUserBlockChoiceRepository;
import io.tofpu.speedbridge2.database.repository.user.name.AbstractUserNameRepository;
import io.tofpu.speedbridge2.database.repository.user.score.AbstractUserScoreRepository;

/**
 * An interface for user-based repository.
 */
public interface UserRepositoryFactory {
    /**
     * @return a {@link AbstractUserNameRepository} implementation.
     */
    AbstractUserNameRepository nameRepository();

    /**
     * @return a {@link AbstractUserScoreRepository} implementation.
     */
    AbstractUserScoreRepository scoreRepository();

    /**
     * @return a {@link AbstractUserBlockChoiceRepository} implementation.
     */
    AbstractUserBlockChoiceRepository blockChoiceRepository();
}
