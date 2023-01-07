package io.tofpu.speedbridge2.service;

import io.tofpu.speedbridge2.database.repository.factory.RepositoryFactoryMaker;
import io.tofpu.speedbridge2.database.repository.factory.exception.InvalidRepositoryException;
import io.tofpu.speedbridge2.database.storage.SQLiteStorage;
import io.tofpu.speedbridge2.service.factory.ServiceFactory;
import io.tofpu.speedbridge2.service.factory.ServiceFactoryMaker;
import io.tofpu.speedbridge2.service.factory.exception.InvalidServiceException;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceFactoryTest {
    @Test
    void test_service_factory() throws InvalidServiceException, InvalidRepositoryException {
        final BaseStorage storage = new SQLiteStorage();
        final ServiceFactory serviceFactory =
                ServiceFactoryMaker.makeFactory(
                        ServiceFactoryMaker.ServiceType.DEFAULT,
                        RepositoryFactoryMaker.RepositoryType.SQ_LITE,
                        storage);

        assertNotNull(serviceFactory.createUserService());
    }
}