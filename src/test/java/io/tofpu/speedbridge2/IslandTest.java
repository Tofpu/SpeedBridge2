package io.tofpu.speedbridge2;

import io.ebean.DB;
import io.tofpu.speedbridge2.domain.Island;
import org.junit.Test;

public class IslandTest {

    @Test
    public void insertFindDelete() {
        final Island island = new Island(1);
        island.save();

        final Island foundIsland = DB.find(Island.class, 1);

        assert foundIsland != null;
        foundIsland.delete();
    }
}
