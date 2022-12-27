package io.tofpu.speedbridge2.database.user.repository.score;

import io.tofpu.speedbridge2.database.storage.StorageUtil;
import io.tofpu.speedbridge2.database.user.repository.score.key.ScoreUUID;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.sql.table.DefaultRepositoryTable;
import io.tofpu.speedbridge2.sql.table.RepositoryTable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static io.tofpu.speedbridge2.sql.StatementQuery.query;

public class DefaultUserScoreRepository extends AbstractUserScoreRepository {
    private static final String INSERT_SQL = "INSERT INTO scores (id, island_slot, score) VALUES (?, ?, ?)";
    private static final String FETCH_SQL = "SELECT * FROM scores WHERE id = ? AND island_slot = ?";
    private static final String DELETE_SQL = "DELETE FROM scores WHERE id = ? AND island_slot = ?";
    private final DefaultRepositoryTable repositoryTable;

    public DefaultUserScoreRepository(final BaseStorage storage) {
        super(storage);
        this.repositoryTable = new DefaultRepositoryTable("scores", "id BLOB UNIQUE PRIMARY KEY", "island_slot INTEGER NOT NULL", "score REAL NOT NULL");
    }

    @Override
    public CompletableFuture<Score> fetch(final ScoreUUID key) {
        final UUID id = key.getUuid();
        final int keySlot = key.getIslandSlot();

        return storage.asyncThreadExecutor().supplyAsync(() -> {
            return query(storage.getConnection(), FETCH_SQL)
                    .setBlob(1, StorageUtil.uidAsByte(id))
                    .setInteger(2, keySlot)
                    .execute()
                    .returnSingleSet(resultRetrieval -> {
                        if (resultRetrieval == null) {
                            return null;
                        }
                        final int islandSlot = resultRetrieval.getInteger("island_slot");
                        final double score = resultRetrieval.getDouble("score");
                        return Score.of(islandSlot, score);
                    });
        });
    }

    @Override
    public CompletableFuture<Boolean> isPresent(final ScoreUUID key) {
        return fetch(key).thenApply(score -> score != null);
    }

    @Override
    public CompletableFuture<Void> insert(final ScoreUUID key, final Score obj) {
        final UUID uuid = key.getUuid();
        return storage.asyncThreadExecutor().runAsync(() -> {
            query(storage.getConnection(), INSERT_SQL)
                    .setBlob(1, StorageUtil.uidAsByte(uuid))
                    .setInteger(2, obj.getScoredOn())
                    .setDouble(3, obj.getScore())
                    .execute();
        });
    }

    @Override
    public CompletableFuture<Void> delete(final ScoreUUID key) {
        final UUID uuid = key.getUuid();
        final int islandSlot = key.getIslandSlot();

        return storage.asyncThreadExecutor().runAsync(() -> {
            query(storage.getConnection(), DELETE_SQL)
                    .setBlob(1, StorageUtil.uidAsByte(uuid))
                    .setInteger(2, islandSlot)
                    .execute();
        });
    }

    @Override
    public RepositoryTable getTable() {
        return repositoryTable;
    }
}