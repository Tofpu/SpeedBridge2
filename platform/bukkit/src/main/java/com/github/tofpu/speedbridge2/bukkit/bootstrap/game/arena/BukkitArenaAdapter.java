package com.github.tofpu.speedbridge2.bukkit.bootstrap.game.arena;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.bukkit.helper.CoreConversionHelper;
import com.github.tofpu.speedbridge2.common.gameextra.ClipboardPaster;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.common.schematic.SchematicResolver;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

public class BukkitArenaAdapter implements PlatformArenaAdapter {
    private final Plugin plugin;

    private static final String GAME_WORLD_NAME = "speedbridge2";
    private static final String[] SUPPORTED_SCHEMATICS_EXTENSIONS = {"schematic", "schem"};

    public BukkitArenaAdapter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void resetAndLoadGameWorld() {
        org.bukkit.World world = Bukkit.getWorld(GAME_WORLD_NAME);
        if (world != null) {
            Bukkit.unloadWorld(world, false);
        }
        File worldFolder = new File(GAME_WORLD_NAME);
        try {
            FileUtils.deleteDirectory(worldFolder);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete " + worldFolder.getName() + " world", e);
        }
        loadGameWorld(); // creates the game world
    }

    @Override
    public World gameWorld() {
        org.bukkit.World world = Bukkit.getWorld(GAME_WORLD_NAME);
        if (world == null) {
            throw new IllegalStateException(String.format("The game world %s must be loaded before using it.", GAME_WORLD_NAME));
        }
        return CoreConversionHelper.toWorld(world);
    }

    private org.bukkit.World bukkitGameWorld() {
        return loadGameWorld();
    }

    private static org.bukkit.World loadGameWorld() {
        return Bukkit.createWorld(WorldCreator.name(GAME_WORLD_NAME)
                .generator(new EmptyChunkGenerator()));
    }

    @Override
    public ClipboardPaster clipboardPaster() {
        if (MultiWorldEditAPI.getMultiWorldEdit() == null) {
            MultiWorldEditAPI.load(plugin);
        }
        return new WorldEditPaster(MultiWorldEditAPI.getMultiWorldEdit());
    }

    @Override
    public SchematicResolver schematicResolver() {
        if (MultiWorldEditAPI.getMultiWorldEdit() == null) {
            MultiWorldEditAPI.load(plugin);
        }
        return new BukkitSchematicResolver(MultiWorldEditAPI.getMultiWorldEdit());
    }

    @Override
    public Predicate<String> schematicPredicate() {
        return schematicFileName -> {
            String extension = FilenameUtils.getExtension(schematicFileName);
            return Arrays.asList(SUPPORTED_SCHEMATICS_EXTENSIONS).contains(extension);
        };
    }

    private static final class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public ChunkData generateChunkData(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
            return createChunkData(world);
        }
    }
}
