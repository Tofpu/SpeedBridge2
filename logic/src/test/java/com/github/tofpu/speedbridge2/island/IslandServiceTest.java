package com.github.tofpu.speedbridge2.island;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class IslandServiceTest {
    private final DatabaseService databaseService = new DatabaseService();
    private final IslandService islandService = new IslandService(databaseService);

    @BeforeEach
    void setUp() {
        databaseService.load();
    }

    @AfterEach
    void tearDown() {
        databaseService.unload();
    }

    @Test
    void register_and_compare_from_db_test() {
        Location origin = new Location(new World("dummy"), 1, 1, 1, 1, 1);
        File schematicFile = new File("");
        islandService.register(1, origin, schematicFile);

        AtomicReference<Island> islandFromDb = new AtomicReference<>();
        databaseService.executeSync(session -> {
            islandFromDb.set(session.find(Island.class, 1));
        });

        Assertions.assertEquals(islandFromDb.get(), islandService.get(1));
    }
}
