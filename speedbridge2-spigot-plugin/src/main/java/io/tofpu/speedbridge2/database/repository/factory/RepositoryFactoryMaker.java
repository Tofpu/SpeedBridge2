package io.tofpu.speedbridge2.database.repository.factory;

import io.tofpu.speedbridge2.database.repository.factory.exception.InvalidRepositoryException;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

/**
 * Responsible for creation of {@link RepositoryFactory}
 *
 * @see RepositoryFactory
 * @see RepositoryFactoryMaker.RepositoryType
 */
public class RepositoryFactoryMaker {
    /**
     * Returns a {@link RepositoryFactory} implementation that matches with the repository type.
     *
     * @param repositoryType repository type of your choice.
     * @param storage storage implementation of your choice.
     *
     * @return a {@link RepositoryFactory} implementation that matches with the repository type.
     *
     * @throws InvalidRepositoryException if the given repositoryType does not exist.
     */
    public static RepositoryFactory makeFactory(
            final RepositoryType repositoryType, final BaseStorage storage) throws InvalidRepositoryException {
        if (repositoryType == RepositoryType.SQ_LITE) {
            return new DefaultRepositoryFactory(storage);
        }
        throw new InvalidRepositoryException(repositoryType);
    }

    public enum RepositoryType {
        SQ_LITE;
    }
}
