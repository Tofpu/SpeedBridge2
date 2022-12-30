package io.tofpu.speedbridge2.database.user.repository.block;

import io.tofpu.speedbridge2.database.user.repository.RepositoryUtil;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.sql.table.DefaultRepositoryTable;
import io.tofpu.speedbridge2.sql.table.RepositoryTable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static io.tofpu.speedbridge2.database.user.repository.RepositoryUtil.simpleFetch;

public class DefaultUserBlockChoiceRepository extends AbstractUserBlockChoiceRepository {
    private static final String INSERT_SQL = "INSERT INTO blocks (id, chosen_block) VALUES (?, ?)";
    private static final String FETCH_SQL = "SELECT * FROM blocks WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM blocks WHERE id = ?";

    private final DefaultRepositoryTable repositoryTable;

    // todo: change column name from "chosen_block" to "choice-block
    public DefaultUserBlockChoiceRepository(final BaseStorage storage) {
        super(storage);
        this.repositoryTable = new DefaultRepositoryTable("blocks", "id BLOB UNIQUE PRIMARY KEY", "chosen_block TEXT NOT NULL");
    }

    @Override
    public CompletableFuture<String> fetch(final UUID key) {
        return simpleFetch(storage, FETCH_SQL, key, resultRetrieval -> {
            if (resultRetrieval == null) {
                return null;
            }
            return resultRetrieval.getString("chosen_block");
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
