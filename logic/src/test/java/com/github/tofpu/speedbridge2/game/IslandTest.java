package com.github.tofpu.speedbridge2.game;

import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.DatabaseBuilder;
import com.github.tofpu.speedbridge2.database.DatabaseFactoryMaker;
import com.github.tofpu.speedbridge2.database.DatabaseType;
import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class IslandTest {
    private final Database database = DatabaseBuilder.create("com.github.tofpu.speedbridge2")
            .data(ConnectionDetails.MEMORY)
            .build(DatabaseType.H2, DatabaseFactoryMaker.sync());

    @Test
    void island_basic() {
        Island island = new Island(1, new Island.IslandSchematic(new Location(new World("test"), 0, 0, 0, 0, 0), new File("test-resources/island/schematics/test.schem")));
        database.execute(session -> session.persist(island));
        database.execute(session -> Assertions.assertEquals(island, session.get(Island.class, 1)));
    }
}