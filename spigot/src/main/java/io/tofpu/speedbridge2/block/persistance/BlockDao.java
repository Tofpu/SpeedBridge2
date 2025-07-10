package io.tofpu.speedbridge2.block.persistance;

import io.tofpu.speedbridge2.database.infra.db.Database;
import org.jooq.DSLContext;
import org.jooq.generated.tables.Blocks;
import org.jooq.generated.tables.records.BlocksRecord;
import org.jooq.impl.DSL;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BlockDao {
    private final Database database;

    public BlockDao(Database database) {
        this.database = database;
    }

    public CompletableFuture<String> find(UUID playerId) {
        return database.supplyAsync(connection -> {
            DSLContext dsl = DSL.using(connection);
            BlocksRecord record = dsl.selectFrom(Blocks.BLOCKS)
                    .where(Blocks.BLOCKS.PLAYER_ID.eq(playerId))
                    .fetchAny();
            if (record == null) {
                return null;
            }
            return record.getBlockId();
        });
    }

    public CompletableFuture<Void> save(UUID playerId, String blockId) {
        return database.handleAsync(connection -> {
           DSLContext dsl = DSL.using(connection);
            dsl.insertInto(Blocks.BLOCKS)
                    .set(Blocks.BLOCKS.PLAYER_ID, playerId)
                    .set(Blocks.BLOCKS.BLOCK_ID, blockId)
                    .onDuplicateKeyUpdate()
                    .set(Blocks.BLOCKS.BLOCK_ID, blockId)
                    .execute();
        });
    }

    public CompletableFuture<Void> delete(UUID playerId) {
        return database.handleAsync(connection -> {
            DSLContext dsl = DSL.using(connection);
            dsl.deleteFrom(Blocks.BLOCKS)
                    .where(Blocks.BLOCKS.PLAYER_ID.eq(playerId))
                    .execute();
        });
    }
}
