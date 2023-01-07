package io.tofpu.speedbridge2.database.repository.factory;

import io.tofpu.speedbridge2.database.repository.user.factory.UserRepositoryFactory;

/**
 * An abstract-factory implementation for all repository-factories alike.
 *
 * @see UserRepositoryFactory
 */
public interface RepositoryFactory {
    /**
     * @return a {@link UserRepositoryFactory} implementation.
     */
    UserRepositoryFactory userRepository();
}
