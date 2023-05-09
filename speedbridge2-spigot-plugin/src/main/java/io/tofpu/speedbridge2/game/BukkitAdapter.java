package io.tofpu.speedbridge2.game;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import io.tofpu.speedbridge2.game.bukkit.BukkitBlockType;
import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;
import org.bukkit.Material;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireState;

public class BukkitAdapter {
    public static Position to(final Vector vector) {
        return Position.of(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public static BlockType to(BaseBlock lazyBlock) {
        Material material = Material.getMaterial(lazyBlock.getId());
        requireState(material != null, "There is no block type with id %s", lazyBlock);
        return new BukkitBlockType(material);
    }
}
