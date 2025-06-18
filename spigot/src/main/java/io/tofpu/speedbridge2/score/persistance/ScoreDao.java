package io.tofpu.speedbridge2.score.persistance;

import io.tofpu.speedbridge2.database.infra.db.Database;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.generated.tables.Scores;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;

public class ScoreDao {
    private final Database database;

    public ScoreDao(Database database) {
        this.database = database;
    }

    public void save(ScoreEntity entity) {
        database.handle(connection -> {
            DSLContext dslContext = DSL.using(connection, SQLDialect.H2);
            dslContext.insertInto(Scores.SCORES)
                    .values(
                            entity.playerId(),
                            entity.slot(),
                            entity.time(),
                            LocalDateTime.ofInstant(entity.timestamp(), ZoneId.systemDefault())
                    )
                    .execute();
        });
    }

    public boolean delete(ScoreEntity entity) {
        return database.supply(connection -> {
            DSLContext dslContext = DSL.using(connection, SQLDialect.H2);
            return dslContext.deleteFrom(Scores.SCORES)
                    .where(
                            Scores.SCORES.PLAYER_ID.eq(entity.playerId()),
                            Scores.SCORES.SLOT.eq(entity.slot()),
                            Scores.SCORES.TIME.eq(entity.time()),
                            Scores.SCORES.CREATED_AT.eq(LocalDateTime.ofInstant(entity.timestamp(), ZoneId.systemDefault()))
                    ).execute() > 0;
        });
    }


    public Collection<ScoreEntity> findAll() {
        return database.supply(connection -> {
            DSLContext dslContext = DSL.using(connection, SQLDialect.H2);
            return dslContext.selectFrom(Scores.SCORES)
                    .fetch()
                    .map(record -> new ScoreEntity(
                            record.getPlayerId(),
                            record.getSlot(),
                            record.getTime(),
                            record.getCreatedAt().toInstant(ZoneOffset.UTC)
                    ));
        });
    }
}
