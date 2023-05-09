package io.tofpu.speedbridge2.game.land;

import io.tofpu.speedbridge2.game.object.FakeLand;
import io.tofpu.speedbridge2.game.arena.land.LandArea;
import io.tofpu.speedbridge2.game.arena.land.LandSchematic;
import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;
import io.tofpu.speedbridge2.game.object.FakeWorld;
import io.tofpu.speedbridge2.game.schematic.DummyBlockType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class LandTest {
    @Test
    void basic() {
        Position schematicOrigin = Position.of(100, 100, 100);
        Position landOrigin = Position.zero();

        final FakeWorld world = new FakeWorld();
        final LandArea landArea = new FakeLand(world, landOrigin);

        final Map<Position, BlockType> blockMap = new HashMap<>();

        // origin
        blockMap.put(schematicOrigin, new DummyBlockType("DIAMOND"));

        // x axis
        blockMap.put(schematicOrigin.add(1, 0, 0), new DummyBlockType("WOOL"));
        blockMap.put(schematicOrigin.add(2, 0, 0), new DummyBlockType("RANDOM_ORE"));

        // y axis
        blockMap.put(schematicOrigin.add(0, 1, 0), new DummyBlockType("WOOL"));
        blockMap.put(schematicOrigin.add(0, 2, 0), new DummyBlockType("RANDOM_ORE"));

        // z axis
        blockMap.put(schematicOrigin.add(0, 0, 1), new DummyBlockType("WOOL"));
        blockMap.put(schematicOrigin.add(0, 0, 2), new DummyBlockType("RANDOM_ORE"));

        final LandSchematic landSchematic = new LandSchematic(schematicOrigin, schematicOrigin.add(2, 2, 2), blockMap);
        landArea.generateLand(landSchematic);

        // verifying that the schematic's blocks were set in relative of the land's location
        Assertions.assertEquals(new DummyBlockType("DIAMOND"), world.getBlockAt(landOrigin));

        Assertions.assertEquals(new DummyBlockType("WOOL"), world.getBlockAt(landOrigin.add(1, 0, 0)));
        Assertions.assertEquals(new DummyBlockType("RANDOM_ORE"), world.getBlockAt(landOrigin.add(2, 0, 0)));

        Assertions.assertEquals(new DummyBlockType("WOOL"), world.getBlockAt(landOrigin.add(0, 1, 0)));
        Assertions.assertEquals(new DummyBlockType("RANDOM_ORE"), world.getBlockAt(landOrigin.add(0, 2, 0)));

        Assertions.assertEquals(new DummyBlockType("WOOL"), world.getBlockAt(landOrigin.add(0, 0, 1)));
        Assertions.assertEquals(new DummyBlockType("RANDOM_ORE"), world.getBlockAt(landOrigin.add(0, 0, 2)));
    }
}
