package io.tofpu.speedbridge2.game;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.biome.BaseBiome;
import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.ClipboardWrapperV6;
import io.tofpu.speedbridge2.game.arena.land.LandSchematic;
import io.tofpu.speedbridge2.game.generic.BlockType;
import io.tofpu.speedbridge2.game.generic.Position;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchematicAdapter {

    public static LandSchematic to(Clipboard clipboard) {
        Position minimumPosition = BukkitAdapter.to(clipboard.getMinimumPoint());
        Position maximumPosition = BukkitAdapter.to(clipboard.getMaximumPoint());

        final Map<Position, BlockType> blockMap = new HashMap<>();
        for (int x = minimumPosition.getX(); x < maximumPosition.getX(); x++) {
            for (int y = minimumPosition.getY(); y < maximumPosition.getY(); y++) {
                for (int z = minimumPosition.getZ(); z < maximumPosition.getZ(); z++) {
                    blockMap.put(Position.of(x, y, z), BukkitAdapter.to(clipboard.getLazyBlock(new BlockVector(x, y, z))));
                }
            }
        }

        return new LandSchematic(minimumPosition, maximumPosition, blockMap);
    }

    public static ClipboardWrapper toClipboard(LandSchematic schematic) {
        Clipboard clipboard = new Clipboard() {
            @Override
            public Region getRegion() {
                return null;
            }

            @Override
            public Vector getDimensions() {
                return null;
            }

            @Override
            public Vector getOrigin() {
                return null;
            }

            @Override
            public void setOrigin(Vector origin) {

            }

            @Override
            public Vector getMinimumPoint() {
                return null;
            }

            @Override
            public Vector getMaximumPoint() {
                return null;
            }

            @Override
            public List<? extends Entity> getEntities(Region region) {
                return null;
            }

            @Override
            public List<? extends Entity> getEntities() {
                return null;
            }

            @Nullable
            @Override
            public Entity createEntity(Location location, BaseEntity entity) {
                return null;
            }

            @Override
            public BaseBlock getBlock(Vector position) {
                return null;
            }

            @Override
            public BaseBlock getLazyBlock(Vector position) {
                return null;
            }

            @Override
            public BaseBiome getBiome(Vector2D position) {
                return null;
            }

            @Override
            public boolean setBlock(Vector position, BaseBlock block) throws WorldEditException {
                return false;
            }

            @Override
            public boolean setBiome(Vector2D position, BaseBiome biome) {
                return false;
            }

            @Nullable
            @Override
            public Operation commit() {
                return null;
            }
        };
        // todo: get rid of this bullshit
        return new ClipboardWrapperV6(clipboard);
    }
}
