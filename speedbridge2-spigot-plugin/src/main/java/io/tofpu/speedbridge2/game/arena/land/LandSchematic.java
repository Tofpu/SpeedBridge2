package io.tofpu.speedbridge2.game.arena.land;

import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;

import java.util.HashMap;
import java.util.Map;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireArgument;

public class LandSchematic {
    private final Position origin;
    private final Position minimumPosition;
    private final Position maximumPosition;
    private final Map<Position, BlockType> blockMap;

    public LandSchematic(Position minimumPosition, Position maximumPosition, Map<Position, BlockType> blockMap) {
        requireArgument(minimumPosition.isLesser(maximumPosition, true), "Minimum position %s must be lesser than or equal to the maximum position %s", minimumPosition, maximumPosition);

        this.origin = minimumPosition;
        this.minimumPosition = minimumPosition;
        this.maximumPosition = maximumPosition;
        this.blockMap = new HashMap<>(blockMap);

        requireArgument(getVolume() >= blockMap.size(), "%s blocks exceeded blocks volume of", blockMap.size(), getVolume());
        if (!blockMap.isEmpty()) {
            requireArgument(blockMap.keySet().stream().allMatch(position -> position.isGreater(minimumPosition, true)), "A block must be greater than or equal to the minimum position of %s", minimumPosition);
            requireArgument(blockMap.keySet().stream().allMatch(position -> position.isLesser(maximumPosition, true)), "A block must be lesser than or equal to the maximum position of %s", maximumPosition);
        }
    }

    public static LandSchematic empty() {
        return new LandSchematic(Position.zero(), Position.zero(), new HashMap<>());
    }

    public int getWidth() {
        return (this.maximumPosition.getX() - this.minimumPosition.getX()) + 1;
    }

    public int getHeight() {
        return (this.maximumPosition.getY() - this.minimumPosition.getY()) + 1;
    }

    public int getLength() {
        return (this.maximumPosition.getZ() - this.minimumPosition.getZ()) + 1;
    }

    public int getVolume() {
        return getWidth() * getHeight() * getLength();
    }

    public Position getOrigin() {
        return origin;
    }

    public Map<Position, BlockType> getBlockMap() {
        return blockMap;
    }
}