package io.tofpu.speedbridge2.database.user.repository.block;

import io.tofpu.speedbridge2.database.storage.StorageUtil;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.sql.table.DefaultRepositoryTable;
import io.tofpu.speedbridge2.sql.table.RepositoryTable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static io.tofpu.speedbridge2.sql.StatementQuery.query;

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
        return storage.asyncThreadExecutor().supplyAsync(() -> {
            return query(storage.getConnection(), FETCH_SQL)
                    .setBlob(1, StorageUtil.uidAsByte(key))
                    .execute()
                    .returnSingleSet(resultRetrieval -> {
                        if (resultRetrieval == null) {
                            return null;
                        }
                        return resultRetrieval.getString("chosen_block");
                    });
        });
    }

    @Override
    public CompletableFuture<Boolean> isPresent(final UUID key) {
        return fetch(key).thenApply(bridgePlayer -> bridgePlayer != null);
    }

    @Override
    public CompletableFuture<Void> insert(final UUID key, final String obj) {
        return storage.asyncThreadExecutor().runAsync(() -> {
            query(storage.getConnection(), INSERT_SQL)
                    .setBlob(1, StorageUtil.uidAsByte(key))
                    .setString(2, obj)
                    .execute();
        });
    }

    @Override
    public CompletableFuture<Void> delete(final UUID key) {
        return storage.asyncThreadExecutor().runAsync(() -> {
            query(storage.getConnection(), DELETE_SQL)
                    .setBlob(1, StorageUtil.uidAsByte(key))
                    .execute();
        });
    }

    @Override
    public RepositoryTable getTable() {
        return repositoryTable;
    }
}
