package io.tofpu.speedbridge2.game.object;

import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;
import io.tofpu.speedbridge2.game.generic.World;
import io.tofpu.speedbridge2.game.schematic.DummyBlockType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FakeWorld extends World {
    private final Map<Position, BlockType> positionBlockMap = new HashMap<>();

    @Override
    public void setBlock(BlockType blockType, Position at) {
        this.positionBlockMap.put(at, blockType);
    }

    @Override
    public @NotNull BlockType getBlockAt(Position position) {
        return this.positionBlockMap.getOrDefault(position, getDefaultBlockType());
    }

    @Override
    public @NotNull BlockType getDefaultBlockType() {
        return new DummyBlockType("AIR");
    }
}
