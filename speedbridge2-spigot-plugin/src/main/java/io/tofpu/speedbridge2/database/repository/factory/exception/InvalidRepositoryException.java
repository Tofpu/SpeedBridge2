package io.tofpu.speedbridge2.database.repository.factory.exception;

import io.tofpu.speedbridge2.database.repository.factory.RepositoryFactoryMaker;

/**
 * Thrown when an unknown repository type were used.
 */
public class InvalidRepositoryException extends Throwable {
    public InvalidRepositoryException(final RepositoryFactoryMaker.RepositoryType repositoryType) {
        super("Unknown repository type: " + repositoryType.name());
    }
}
