package io.tofpu.speedbridge2.group.domain;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface GroupRepository {
    CompletableFuture<Void> save(Group group);
    CompletableFuture<Group> findById(UUID id);
    CompletableFuture<Collection<Group>> findAll();
    CompletableFuture<Boolean> removeById(UUID id);
}
