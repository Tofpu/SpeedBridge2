package io.tofpu.speedbridge2.database.repository.user.name;

import io.tofpu.speedbridge2.database.repository.RepositoryUtil;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.sql.table.DefaultRepositoryTable;
import io.tofpu.speedbridge2.sql.table.RepositoryTable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DefaultUserNameRepository extends AbstractUserNameRepository {
    private static final String INSERT_SQL = "INSERT INTO users (id, name) VALUES (?, ?)";
    private static final String FETCH_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";
    private final DefaultRepositoryTable repositoryTable;

    public DefaultUserNameRepository(final BaseStorage storage) {
        super(storage);
        this.repositoryTable = new DefaultRepositoryTable("users", "id BLOB UNIQUE PRIMARY KEY", "name TEXT NOT NULL");
    }

    @Override
    public CompletableFuture<String> fetch(final UUID key) {
        return RepositoryUtil.simpleFetch(storage, FETCH_SQL, key, resultRetrieval -> {
            if (resultRetrieval == null) {
                return null;
            }
            return resultRetrieval.getString("name");
        });
    }

    @Override
    public CompletableFuture<Boolean> isPresent(final UUID key) {
        return fetch(key).thenApply(bridgePlayer -> bridgePlayer != null);
    }

    @Override
    public CompletableFuture<Void> insert(final UUID key, final String obj) {
        return RepositoryUtil.complexExecute(storage, INSERT_SQL, key, statementQuery -> statementQuery.setString(2, obj));
    }

    @Override
    public CompletableFuture<Void> delete(final UUID key) {
        return RepositoryUtil.simpleExecute(storage, DELETE_SQL, key);
    }

    @Override
    public RepositoryTable getTable() {
        return repositoryTable;
    }
}