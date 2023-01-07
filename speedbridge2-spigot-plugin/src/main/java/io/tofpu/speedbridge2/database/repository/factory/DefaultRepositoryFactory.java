package io.tofpu.speedbridge2.database.repository.factory;

import io.tofpu.speedbridge2.database.repository.user.factory.DefaultUserRepositoryFactory;
import io.tofpu.speedbridge2.database.repository.user.factory.UserRepositoryFactory;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

public class DefaultRepositoryFactory implements RepositoryFactory {
    private final BaseStorage storage;

    public DefaultRepositoryFactory(final BaseStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserRepositoryFactory userRepository() {
        return new DefaultUserRepositoryFactory(storage);
    }
}
