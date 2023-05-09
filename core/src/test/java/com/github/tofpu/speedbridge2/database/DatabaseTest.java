package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseTest {
    private final Database database = DatabaseBuilder.create("com.github.tofpu.speedbridge2")
            .data(ConnectionDetails.MEMORY)
            .operationType(OperationType.ASYNC)
            .build(DatabaseType.H2);

    @AfterEach
    void tearDown() {
        this.database.shutdown();
    }

    @Test
    void sanity_check() {
        // I know this looks like insanity, but basically this test
        // verifies that database's compute is actually working
        // and not bogus
        Thread mainThread = Thread.currentThread();

        AtomicReference<Throwable> exception = new AtomicReference<>();
        mainThread.setUncaughtExceptionHandler((t, e) -> exception.set(e));

        Assertions.assertThrows(RuntimeException.class, () -> {
            database.compute(session -> mainThread.getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), new RuntimeException()));
            Thread.sleep(2000);
            if (exception.get() != null) {
                throw new RuntimeException(exception.get());
            }
        });
    }

    @Test
    void async_operation_test() {
        final Database localDatabase = DatabaseBuilder.create("com.github.tofpu.speedbridge2")
                .data(ConnectionDetails.MEMORY)
                .operationType(OperationType.ASYNC)
                .build(DatabaseType.H2);

        final Thread mainThread = Thread.currentThread();
        localDatabase.compute(session -> Assertions.assertNotEquals(mainThread.getId(), Thread.currentThread().getId()));

        localDatabase.shutdown();
    }

    @Test
    void test_file_based_database() {
        final Database localDatabase = DatabaseBuilder.create("com.github.tofpu.speedbridge2")
                .data(ConnectionDetails.file("/test-resources/test.db"))
                .operationType(OperationType.ASYNC)
                .build(DatabaseType.H2);

        localDatabase.compute(session -> Assertions.assertTrue(session.isConnected()));
        localDatabase.shutdown();
    }

    @Test
    void basic_modify_and_retrieval_operation_test() {
        UUID id = UUID.randomUUID();
        database.compute(session -> session.persist(new DemoEntity(id, 2)));
        database.compute(session -> session.get(DemoEntity.class, id).number(20));
        database.compute(session -> {
            DemoEntity demoEntity = session.get(DemoEntity.class, id);
            Assertions.assertEquals(20, demoEntity.getNumber());
        });
    }
}
