package io.tofpu.speedbridge2.island.domain;

import java.util.Collection;

public interface IslandRepository {
    void saveOrUpdate(Island island);
    Island findBySlot(int slot);
    Collection<ValidatableIsland> findAll();
    boolean deleteBySlot(int slot);
}
