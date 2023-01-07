package io.tofpu.speedbridge2.service.factory.exception;

import io.tofpu.speedbridge2.service.factory.ServiceFactoryMaker;

/**
 * Thrown when an unknown service type were used.
 */
public class InvalidServiceException extends Throwable {
    public InvalidServiceException(final ServiceFactoryMaker.ServiceType serviceType) {
        super("Unknown service type: " + serviceType.name());
    }
}
