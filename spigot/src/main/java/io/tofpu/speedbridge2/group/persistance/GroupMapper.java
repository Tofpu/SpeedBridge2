package io.tofpu.speedbridge2.group.persistance;

import io.tofpu.speedbridge2.group.domain.Group;

public class GroupMapper {
    public GroupEntity toEntity(Group domain) {
        return new GroupEntity(
                domain.id(),
                domain.name()
        );
    }

    public Group toDomain(GroupEntity entity) {
        return new Group(
                entity.id(),
                entity.name()
        );
    }
}
