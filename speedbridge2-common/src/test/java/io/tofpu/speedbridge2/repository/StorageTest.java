package io.tofpu.speedbridge2.repository;

import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StorageTest {
    @Test
    public void test_asyncthreadexecutor_not_null() {
        final BaseStorage storage = new RepositoryTest.BasicSQLiteStorage();
        assertNotNull(storage.asyncThreadExecutor());
    }
}