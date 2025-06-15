package io.tofpu.speedbridge2.island.persistence;

import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.domain.IslandRepository;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;

import java.util.Collection;
import java.util.List;

public class IslandRepositoryImpl implements IslandRepository {
    private final IslandDao islandDao;
    private final IslandMapper islandMapper;
    private final SchematicHandler schematicHandler;

    public IslandRepositoryImpl(IslandDao islandDao, IslandMapper islandMapper, SchematicHandler schematicHandler) {
        this.islandDao = islandDao;
        this.islandMapper = islandMapper;
        this.schematicHandler = schematicHandler;
    }

    @Override
    public void save(Island island) {
        IslandEntity entity = islandMapper.toEntity(island);
        islandDao.save(entity);
    }

    @Override
    public Island findBySlot(int slot) {
        IslandEntity entity = islandDao.findBySlot(slot);
        Schematic schematic = schematicHandler.resolveSchematic(entity.schematicName());
        return islandMapper.toDomain(entity, schematic);
    }

    @Override
    public Collection<Island> findAll() {
        List<IslandEntity> entities = islandDao.findAll();
        return entities.stream()
                .map(entity -> {
                    Schematic schematic = schematicHandler.resolveSchematic(entity.schematicName());
                    return islandMapper.toDomain(entity, schematic);
                })
                .toList();
    }

    @Override
    public boolean deleteBySlot(int slot) {
        return islandDao.deleteBySlot(slot);
    }
}
