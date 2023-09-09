package com.github.tofpu.speedbridge2.bootstrap.game.arena;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.adapter.BukkitAdapter;
import com.github.tofpu.speedbridge2.game.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.object.World;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class IslandArenaAdapter implements ArenaAdapter {

    public static final String GAME_WORLD_NAME = "speedbridge2";

    @Override
    public World gameWorld() {
        return BukkitAdapter.toWorld(Bukkit.createWorld(WorldCreator.name(GAME_WORLD_NAME)
                .generator(new EmptyChunkGenerator())));
    }

    @Override
    public ClipboardPaster clipboardPaster() {
        return new WorldEditPaster(MultiWorldEditAPI.getMultiWorldEdit());
    }

    private static final class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public ChunkData generateChunkData(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
            return createChunkData(world);
        }
    }
}
