package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseServiceTest {
    private final DatabaseService databaseService = new DatabaseService();

    @BeforeEach
    void setUp() {
        databaseService.load();
    }

    @AfterEach
    void tearDown() {
        databaseService.unload();
    }

    @Test
    void exception_compute_async_test() {
        assertThrows(ExecutionException.class, () -> databaseService.computeAsync(session -> {
            throw new RuntimeException();
        }).get(10, TimeUnit.SECONDS), "An exception was not caught");
    }

    @Test
    void exception_execute_async_test() {
        assertThrows(ExecutionException.class, () -> databaseService.executeAsync(session -> {
            throw new RuntimeException();
        }).get(10, TimeUnit.SECONDS), "An exception was not caught");
    }
}
