package io.tofpu.speedbridge2.domain.repository;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.query.QIsland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IslandRepository {

    public Map<Integer, Island> loadIslands() {
        final List<Island> islands = new ArrayList<>(new QIsland().findList());

        System.out.println("loaded islands:");
        System.out.println(islands);

        final Map<Integer, Island> islandMap = new HashMap<>();
        for (final Island island : islands) {
            islandMap.put(island.getSlot(), island);
        }

        return islandMap;
    }

}
