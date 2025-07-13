package io.tofpu.speedbridge2.group.persistance;

import io.tofpu.speedbridge2.database.infra.db.Database;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.GroupsRecord;
import org.jooq.impl.DSL;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.jooq.generated.tables.Groups.GROUPS;

public class GroupDao {
    private final Database database;

    public GroupDao(Database database) {
        this.database = database;
    }

    public CompletableFuture<GroupEntity> persist(String groupName) {
        return database.supplyAsync(connection -> {
            DSLContext context = DSL.using(connection);
            context.insertInto(GROUPS)
                    .values(0, groupName)
                    .execute();
            GroupsRecord record = context.selectFrom(GROUPS)
                    .where(GROUPS.NAME.eq(groupName))
                    .fetchOne();
            if (record == null) {
                System.out.printf("Failed to create a group with name %s%n", groupName);
                return null;
            }
            return new GroupEntity(
                    record.getId(),
                    record.getName()
            );
        });
    }

    public CompletableFuture<Void> persistOrUpdate(GroupEntity entity) {
        return database.handleAsync(connection -> {
            DSLContext context = DSL.using(connection);
            context.insertInto(GROUPS)
                    .values(entity.id(), entity.name())
                    .onDuplicateKeyUpdate()
                    .set(GROUPS.NAME, entity.name())
                    .execute();
        });
    }

    public CompletableFuture<GroupEntity> fetchById(UUID id) {
        return database.supplyAsync(connection -> {
            DSLContext context = DSL.using(connection);
            GroupsRecord record = context.selectFrom(GROUPS)
                    .where(GROUPS.ID.eq(id))
                    .fetchOne();
            if (record == null) {
                return null;
            }
            return new GroupEntity(
                    id,
                    record.getName()
            );
        });
    }

    public CompletableFuture<Collection<GroupEntity>> fetchAll() {
        return database.supplyAsync(connection -> {
            DSLContext context = DSL.using(connection);
            return context.selectFrom(GROUPS)
                    .fetch()
                    .map(record -> new GroupEntity(
                            record.getId(),
                            record.getName()
                    ));
        });
    }

    public CompletableFuture<Boolean> deleteById(UUID id) {
        return database.supplyAsync(connection -> {
            DSLContext context = DSL.using(connection);
            int rowsAffected = context.delete(GROUPS)
                    .where(GROUPS.ID.eq(id))
                    .execute();
            return rowsAffected > 0;
        });
    }
}
