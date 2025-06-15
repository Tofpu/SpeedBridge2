package io.tofpu.speedbridge2.environment.infra.generator;

import java.util.Random;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public final class EmptyChunkGenerator extends ChunkGenerator {
    public static final EmptyChunkGenerator INSTANCE = new EmptyChunkGenerator();

    private EmptyChunkGenerator() {}

    @Override
    public ChunkData generateChunkData(
            final World world, final Random random, final int x, final int z, final BiomeGrid biome) {
        return createChunkData(world);
    }
}
