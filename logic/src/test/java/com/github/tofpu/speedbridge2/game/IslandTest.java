package com.github.tofpu.speedbridge2.game;

import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.bridge.game.Island;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class IslandTest {
    private final Database database = Database.factory().createH2Database();

    @Test
    void island_basic() {
        Island island = new Island(1, new Island.IslandSchematicData(new Location(new World("test"), 0, 0, 0, 0, 0), new File("test-resources/island/schematics/test.schem")));
        database.executeSync(session -> session.persist(island));
        database.executeSync(session -> Assertions.assertEquals(island, session.get(Island.class, 1)));
    }
}
