package io.tofpu.speedbridge2.game.arena.land;

import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;
import io.tofpu.speedbridge2.game.generic.World;

public abstract class LandArea {
    protected final World world;
    protected final Position position;
    private boolean reserved = false;

    public LandArea(World world, Position position) {
        this.world = world;
        this.position = position;
    }

    public void generateLand(LandSchematic schematic) {
        for (int x = 0; x <= schematic.getWidth(); x++) {
            for (int y = 0; y <= schematic.getHeight(); y++) {
                for (int z = 0; z <= schematic.getWidth(); z++) {
                    Position blockPosition = schematic.getOrigin().add(x, y, z);
                    BlockType blockType = schematic.getBlockMap().get(blockPosition);

                    Position newPosition = position.add(x, y, z);
                    world.setBlock(blockType, newPosition);
                }
            }
        }
    }

    public boolean isReserved() {
        return reserved;
    }

    public void reserved(boolean reserved) {
        this.reserved = reserved;
    }

    public Position getPosition() {
        return position;
    }
}
