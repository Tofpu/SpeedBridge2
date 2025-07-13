package io.tofpu.speedbridge2.group.service;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.group.domain.GroupRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GroupService {
    private final Map<UUID, Group> idToGroupMap = new HashMap<>();
    private final Map<String, Group> nameToGroupMap = new HashMap<>();

    private final GroupRepository repository;

    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public void load() {
        repository.findAll()
                .thenAccept(groups -> groups.forEach(this::registerToCache));
    }

    public boolean contains(String groupName) {
        return nameToGroupMap.get(groupName) != null;
    }

    public CompletableFuture<Group> getOrLoad(UUID id) {
        Group group = idToGroupMap.get(id);
        if (group != null) {
            return CompletableFuture.completedFuture(group);
        }
        return repository.findById(id)
                .thenApply(group1 -> {
                    if (group1 != null) {
                        registerToCache(group1);
                    }
                    return group1;
                });
    }

    public Group get(String groupName) {
        return nameToGroupMap.get(groupName);
    }

    public CompletableFuture<Group> createGroup(String groupName) {
        Group group = new Group(UUID.randomUUID(), groupName);
        return repository.save(group)
                .thenApply(unused -> {
                    registerToCache(group);
                    return group;
                });
    }

    private void registerToCache(Group group) {
        idToGroupMap.put(group.id(), group);
        nameToGroupMap.put(group.name(), group);
    }

    public CompletableFuture<Boolean> remove(Group group) {
        removeFromCache(group);
        return repository.removeById(group.id());
    }

    private void removeFromCache(Group group) {
        idToGroupMap.remove(group.id());
        nameToGroupMap.remove(group.name());
    }

    public Collection<Group> groups() {
        return Collections.unmodifiableCollection(idToGroupMap.values());
    }
}
