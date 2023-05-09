package io.tofpu.speedbridge2.game.generic;

import org.jetbrains.annotations.NotNull;

public abstract class World {
    public abstract void setBlock(final BlockType blockType, final Position at);
    @NotNull
    public abstract BlockType getBlockAt(final Position position);

    @NotNull
    public abstract BlockType getDefaultBlockType();
}
