package io.tofpu.speedbridge2.database.repository.user.factory;

import io.tofpu.speedbridge2.database.repository.user.block.AbstractUserBlockChoiceRepository;
import io.tofpu.speedbridge2.database.repository.user.block.DefaultUserBlockChoiceRepository;
import io.tofpu.speedbridge2.database.repository.user.name.AbstractUserNameRepository;
import io.tofpu.speedbridge2.database.repository.user.name.DefaultUserNameRepository;
import io.tofpu.speedbridge2.database.repository.user.score.AbstractUserScoreRepository;
import io.tofpu.speedbridge2.database.repository.user.score.DefaultUserScoreRepository;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

public class DefaultUserRepositoryFactory implements UserRepositoryFactory {
    private final BaseStorage storage;

    public DefaultUserRepositoryFactory(final BaseStorage storage) {
        this.storage = storage;
    }

    @Override
    public AbstractUserNameRepository nameRepository() {
        return new DefaultUserNameRepository(storage);
    }

    @Override
    public AbstractUserScoreRepository scoreRepository() {
        return new DefaultUserScoreRepository(storage);
    }

    @Override
    public AbstractUserBlockChoiceRepository blockChoiceRepository() {
        return new DefaultUserBlockChoiceRepository(storage);
    }
}
