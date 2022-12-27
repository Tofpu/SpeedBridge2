package io.tofpu.speedbridge2.repository;

import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.sql.StatementQuery.query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepositoryTest {
    @BeforeAll
    public static void cleanUp() {
        final File file = new File("test.db");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Try to add multiple entries to repository, fetch & isPresent said entries")
    void test_add_entries_to_repository_and_fetch_and_is_present_them() throws ExecutionException, InterruptedException {
        final BaseStorage storage = new BasicSQLiteStorage();
        final SQLRepository repository = new SQLRepository(storage);

        storage.init()
                .get();

        repository.insert(1L, "Hello 1!")
                .get();
        repository.insert(2L, "Hello 2!")
                .get();

        // validations here

        assertTrue(repository.isPresent(1L)
                           .get());
        assertEquals(repository.fetch(1L)
                             .get(), "Hello 1!");

        assertTrue(repository.isPresent(2L)
                           .get());
        assertEquals(repository.fetch(2L)
                             .get(), "Hello 2!");

        assertFalse(repository.isPresent(3L)
                            .get());
        assertNull(repository.fetch(3L)
                           .get());
    }

    static class BasicSQLiteStorage extends BaseStorage {
        private static final String CREATE_TABLE_SQL =
                "CREATE TABLE profile (id INTEGER PRIMARY KEY, name STRING NOT NULL)";
        private Connection connection;

        public BasicSQLiteStorage() {
            super(1);
        }

        @Override
        public CompletableFuture<Void> init() {
            return asyncThreadExecutor().runAsync(() -> {
                        try {
                            Class.forName("org.sqlite.JDBC");
                        } catch (ClassNotFoundException e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .thenRun(this::establishConnection)
                    .thenRun(() -> execute(CREATE_TABLE_SQL));
        }

        @Override
        public void establishConnection() {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    static class SQLRepository extends BaseRepository<Long, String> {
        public static final String FETCH_SQL = "SELECT * FROM profile WHERE id = ?";
        public static final String INSERT_SQL = "INSERT INTO profile (id, name) VALUES (?, ?)";

        protected SQLRepository(final BaseStorage storage) {
            super(storage);
        }

        @Override
        public CompletableFuture<String> fetch(final Long key) {
            return storage.asyncThreadExecutor()
                    .supplyAsync(() -> {
                        return query(storage.getConnection(), FETCH_SQL).setLong(1, key)
                                .execute()
                                .returnSingleSet(resultSet -> {
                                    if (resultSet == null) {
                                        return null;
                                    }
                                    return resultSet.getString("name");
                                });
                    });
        }

        @Override
        public CompletableFuture<Boolean> isPresent(final Long key) {
            return fetch(key).thenApply(s -> s != null);
        }

        @Override
        public CompletableFuture<Void> insert(final Long key, final String obj) {
            return storage.asyncThreadExecutor()
                    .runAsync(() -> {
                        query(storage.getConnection(), INSERT_SQL)
                                .setLong(1, key)
                                .setString(2, obj)
                                .execute();
                    });
        }

        @Override
        public CompletableFuture<Void> delete(final Long key) {
            throw new UnsupportedOperationException("Delete is not implemented yet.");
        }
    }
}
