package io.tofpu.speedbridge2.island.persistence;

import io.tofpu.speedbridge2.database.infra.db.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.generated.tables.Islands;
import org.jooq.generated.tables.records.IslandsRecord;
import org.jooq.impl.DSL;

import java.util.List;

public class IslandDao {
    private final Database database;

    public IslandDao(Database database) {
        this.database = database;
    }

    public void saveOrUpdate(IslandEntity entity) {
        database.handle(connection -> {
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            create.insertInto(Islands.ISLANDS)
                    .values(
                            0, // ID is auto-incremented, so we can use 0 or null
                            entity.slot(),
                            entity.groupId(),
                            entity.schematicName(),
                            entity.location().x(),
                            entity.location().y(),
                            entity.location().z(),
                            entity.location().yaw(),
                            entity.location().pitch()
                    )
                    .onDuplicateKeyUpdate()
                    .set(Islands.ISLANDS.GROUP_ID, entity.groupId())
                    .set(Islands.ISLANDS.SCHEMATIC_NAME, entity.schematicName())
                    .set(Islands.ISLANDS.X, entity.location().x())
                    .set(Islands.ISLANDS.Y, entity.location().y())
                    .set(Islands.ISLANDS.Z, entity.location().z())
                    .set(Islands.ISLANDS.YAW, (double) entity.location().yaw())
                    .set(Islands.ISLANDS.PITCH, (double) entity.location().pitch())
                    .execute();
        });
    }

    public IslandEntity findBySlot(int slot) {
        return database.supply(connection -> {
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            @Nullable IslandsRecord island = create.selectFrom(Islands.ISLANDS)
                    .where(Islands.ISLANDS.SLOT.eq(slot)).fetchAny();
            //noinspection DataFlowIssue
            return new IslandEntity(
                    island.getSlot(),
                    island.getGroupId(),
                    island.getSchematicName(),
                    mapToLocationEntity(island)
            );
        });
    }

    public boolean deleteBySlot(int slot) {
        return database.supply(connection -> {
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            int deletedRows = create.deleteFrom(Islands.ISLANDS)
                    .where(Islands.ISLANDS.SLOT.eq(slot))
                    .execute();
            return deletedRows > 0;
        });
    }

    public List<IslandEntity> findAll() {
        return database.supply(connection -> {
            DSLContext create = DSL.using(connection, SQLDialect.H2);
            return create.selectFrom(Islands.ISLANDS)
                    .fetch()
                    .map(record -> new IslandEntity(
                            record.getSlot(),
                            record.getGroupId(),
                            record.getSchematicName(),
                            mapToLocationEntity(record)
                    ));
        });
    }

    private static IslandEntity.@NotNull LocationEntity mapToLocationEntity(IslandsRecord record) {
        return new IslandEntity.LocationEntity(
                record.getX(),
                record.getY(),
                record.getZ(),
                record.getYaw().floatValue(),
                record.getPitch().floatValue()
        );
    }
}
