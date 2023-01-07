package io.tofpu.speedbridge2.service.factory;

import io.tofpu.speedbridge2.service.user.UserService;

/**
 * An abstract-factory implementation for all services alike.
 *
 * @see UserService
 */
public interface ServiceFactory {
    /**
     * @return a {@link UserService} implementation.
     */
    UserService createUserService();
}
