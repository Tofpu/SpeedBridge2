package io.tofpu.speedbridge2.game.bukkit;

import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;
import io.tofpu.speedbridge2.game.generic.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireArgument;

public class BukkitWorld extends World {
    private final org.bukkit.World world;

    public static World create(org.bukkit.World world) {
        requireArgument(world != null, "World must not be null");
        return new BukkitWorld(world);
    }

    private BukkitWorld(org.bukkit.World world) {
        this.world = world;
    }

    @Override
    public void setBlock(BlockType blockType, Position at) {
        requireArgument(blockType instanceof BukkitBlockType, "Block %s must be an instance of BukkitWorld", blockType.getClass().getSimpleName());
        BukkitBlockType bukkitBlockType = (BukkitBlockType) blockType;
        world.getBlockAt(at.getX(), at.getY(), at.getZ()).setType(bukkitBlockType.getMaterial());

    }

    @Override
    public @NotNull BlockType getBlockAt(Position position) {
        Block blockAt = world.getBlockAt(position.getX(), position.getY(), position.getZ());
        return new BukkitBlockType(blockAt.getType());
    }

    @Override
    public @NotNull BlockType getDefaultBlockType() {
        return new BukkitBlockType(Material.AIR);
    }
}