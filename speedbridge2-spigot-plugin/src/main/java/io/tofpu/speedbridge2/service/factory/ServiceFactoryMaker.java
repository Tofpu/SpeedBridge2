package io.tofpu.speedbridge2.service.factory;

import io.tofpu.speedbridge2.database.repository.factory.RepositoryFactory;
import io.tofpu.speedbridge2.database.repository.factory.RepositoryFactoryMaker;
import io.tofpu.speedbridge2.database.repository.factory.exception.InvalidRepositoryException;
import io.tofpu.speedbridge2.service.factory.exception.InvalidServiceException;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;

import static org.apache.commons.lang.Validate.notNull;

/**
 * Responsible for creation of {@link ServiceFactory}
 *
 * @see ServiceFactory
 * @see ServiceType
 */
public class ServiceFactoryMaker {
    private static ServiceFactory makeFactory(
            final ServiceType serviceType, final RepositoryFactory repositoryFactory) throws InvalidServiceException {
        notNull(serviceType);
        notNull(repositoryFactory);

        if (serviceType == ServiceType.DEFAULT) {
            return new DefaultServiceFactory(repositoryFactory);
        }
        throw new InvalidServiceException(serviceType);
    }

    /**
     * Return a {@link ServiceFactory} implementation that matches with the service type.
     *
     * @param serviceType service type of your choice.
     * @param repositoryType repository type of your choice.
     * @param storage storage implementation of your choice.
     *
     * @return a {@link ServiceFactory} implementation that matches with the service type.
     *
     * @throws InvalidRepositoryException if the given repositoryType does not exist.
     * @throws InvalidServiceException if the given serviceType does not exist.
     */
    public static ServiceFactory makeFactory(
            final ServiceType serviceType, final RepositoryFactoryMaker.RepositoryType repositoryType, final BaseStorage storage)
            throws InvalidRepositoryException, InvalidServiceException {
        final RepositoryFactory repositoryFactory =
                RepositoryFactoryMaker.makeFactory(repositoryType, storage);
        return makeFactory(serviceType, repositoryFactory);
    }

    /**
     * List of  implementation types.
     */
    public enum ServiceType {
        /**
         * The default implementation of Service.
         */
        DEFAULT
    }
}
