package io.tofpu.speedbridge2.island.persistence;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.group.service.GroupService;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.domain.IslandRepository;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class IslandRepositoryImpl implements IslandRepository {
    private static final Logger log = LoggerFactory.getLogger(IslandRepositoryImpl.class);
    private final IslandDao islandDao;
    private final IslandMapper islandMapper;
    private final SchematicHandler schematicHandler;
    private final GroupService groupService;

    public IslandRepositoryImpl(IslandDao islandDao, IslandMapper islandMapper, SchematicHandler schematicHandler, GroupService groupService) {
        this.islandDao = islandDao;
        this.islandMapper = islandMapper;
        this.schematicHandler = schematicHandler;
        this.groupService = groupService;
    }

    @Override
    public void saveOrUpdate(Island island) {
        IslandEntity entity = islandMapper.toEntity(island);
        islandDao.saveOrUpdate(entity);
    }

    @Override
    public Island findBySlot(int slot) {
        IslandEntity entity = islandDao.findBySlot(slot);
        return islandToDomain(entity);
    }

    private Island islandToDomain(IslandEntity entity) {
        Schematic schematic = schematicHandler.resolveSchematic(entity.schematicName());
        Group group = groupService.getOrLoad(entity.groupId()).join();
        if (group == null) {
            System.out.printf("Cannot load island %s because group %s couldn't be found.%n", entity.slot(), entity.groupId());
            return null;
        }
        return islandMapper.toDomain(entity, group, schematic);
    }

    @Override
    public Collection<Island> findAll() {
        List<IslandEntity> entities = islandDao.findAll();
        return entities.stream()
                .map(this::islandToDomain)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public boolean deleteBySlot(int slot) {
        return islandDao.deleteBySlot(slot);
    }
}
