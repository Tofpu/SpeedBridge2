package io.tofpu.speedbridge2.group.persistance;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.group.domain.GroupCreateRequest;
import io.tofpu.speedbridge2.group.domain.GroupRepository;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GroupRepositoryImpl implements GroupRepository {
    private final GroupDao dao;
    private final GroupMapper mapper;

    public GroupRepositoryImpl(GroupDao dao, GroupMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<Void> save(Group group) {
        GroupEntity entity = mapper.toEntity(group);
        return dao.persistOrUpdate(entity);
    }

    @Override
    public CompletableFuture<Group> findById(UUID id) {
        return dao.fetchById(id)
                .thenApply(entity -> {
                    if (entity == null) return null;
                    return mapper.toDomain(entity);
                });
    }

    @Override
    public CompletableFuture<Collection<Group>> findAll() {
        return dao.fetchAll()
                .thenApply(groupEntities -> groupEntities.stream()
                        .map(mapper::toDomain)
                        .toList());
    }

    @Override
    public CompletableFuture<Boolean> removeById(UUID id) {
        return dao.deleteById(id);
    }
}
