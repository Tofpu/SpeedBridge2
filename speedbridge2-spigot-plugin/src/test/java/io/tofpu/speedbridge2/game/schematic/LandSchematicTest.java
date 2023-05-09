package io.tofpu.speedbridge2.game.schematic;

import io.tofpu.speedbridge2.game.arena.land.LandSchematic;
import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

public class LandSchematicTest {
    @Test
    void volume_correctness_verification() {
        Assertions.assertEquals(1, new LandSchematic(Position.zero(), Position.zero(), Collections.emptyMap()).getVolume());
        Assertions.assertEquals(2, new LandSchematic(Position.zero(), Position.of(1, 0, 0), Collections.emptyMap()).getVolume());
        Assertions.assertEquals(2, new LandSchematic(Position.zero(), Position.of(0, 1, 0), Collections.emptyMap()).getVolume());
        Assertions.assertEquals(2, new LandSchematic(Position.zero(), Position.of(0, 0, 1), Collections.emptyMap()).getVolume());
        Assertions.assertEquals(4, new LandSchematic(Position.zero(), Position.of(0, 1, 1), Collections.emptyMap()).getVolume());
        Assertions.assertEquals(8, new LandSchematic(Position.zero(), Position.of(1, 1, 1), Collections.emptyMap()).getVolume());
    }

    @Test
    void argument_correctness_check() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            HashMap<Position, BlockType> blockMap = new HashMap<>();
            blockMap.put(Position.zero(), new DummyBlockType("air"));
            blockMap.put(Position.of(1, 1, 1), new DummyBlockType("air"));
            new LandSchematic(Position.zero(), Position.zero(), blockMap);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            HashMap<Position, BlockType> blockMap = new HashMap<>();
            blockMap.put(Position.zero(), new DummyBlockType("air"));
            new LandSchematic(Position.of(10, 10, 10), Position.of(20, 20, 20), blockMap);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            HashMap<Position, BlockType> blockMap = new HashMap<>();
            blockMap.put(Position.of(2, 2, 2), new DummyBlockType("air"));
            new LandSchematic(Position.zero(), Position.of(1, 1, 1), blockMap);
        });

        Assertions.assertDoesNotThrow(() -> {
            HashMap<Position, BlockType> blockMap = new HashMap<>();
            blockMap.put(Position.of(2, 2, 2), new DummyBlockType("air"));
            new LandSchematic(Position.zero(), Position.of(2, 2, 2), blockMap);
        });
    }
}
