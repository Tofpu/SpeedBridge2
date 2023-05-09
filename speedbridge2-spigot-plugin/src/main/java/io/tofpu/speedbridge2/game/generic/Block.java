package io.tofpu.speedbridge2.game.generic;

public abstract class Block {
    private final World world;
    private final Position position;

    public Block(World world, Position position) {
        this.world = world;
        this.position = position;
    }

    public World getWorld() {
        return world;
    }

    public Position getPosition() {
        return position;
    }
}
