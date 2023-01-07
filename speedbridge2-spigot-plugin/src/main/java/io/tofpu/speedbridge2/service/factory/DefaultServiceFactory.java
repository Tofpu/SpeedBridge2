package io.tofpu.speedbridge2.service.factory;

import io.tofpu.speedbridge2.database.repository.factory.RepositoryFactory;
import io.tofpu.speedbridge2.service.user.DefaultUserService;
import io.tofpu.speedbridge2.service.user.UserService;

public class DefaultServiceFactory implements ServiceFactory {
    private final RepositoryFactory repositoryFactory;

    public DefaultServiceFactory(final RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public UserService createUserService() {
        return new DefaultUserService(repositoryFactory.userRepository());
    }
}
