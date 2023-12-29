package com.github.tofpu.speedbridge2.bukkit.bootstrap.game.arena;

import com.github.tofpu.speedbridge2.bukkit.helper.BukkitConversionHelper;
import com.github.tofpu.speedbridge2.common.game.ClipboardPaster;
import com.github.tofpu.speedbridge2.common.game.land.arena.RegionInfo;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.World;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import io.tofpu.multiworldedit.*;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;

public class WorldEditPaster extends ClipboardPaster {
    private final MultiWorldEdit multiWorldEdit;

    public WorldEditPaster(MultiWorldEdit multiWorldEdit) {
        this.multiWorldEdit = multiWorldEdit;
    }

    @Override
    public RegionInfo getRegion(File file) {
        ClipboardWrapper clipboardWrapper = multiWorldEdit.read(file);
        Region blockVectors = clipboardWrapper.region().to();
        return new RegionInfo(blockVectors.getWidth(), blockVectors.getHeight(), toVector(clipboardWrapper.getOrigin()), toVector(clipboardWrapper.getMinimumPoint()), toVector(clipboardWrapper.getMaximumPoint()));
    }

    private static Vector toVector(VectorWrapper vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void paste(File schematicFile, Position position) {
        ClipboardWrapper schematic = multiWorldEdit.read(schematicFile);
        BukkitWorld world = new BukkitWorld(BukkitConversionHelper.toWorld(position.getWorld()));
        try (final EditSessionWrapper editSessionWrapper = multiWorldEdit
                .create(world, -1)) {
            final Clipboard schematicClipboard = schematic.to();
            final EditSession editSession = editSessionWrapper.to();

            final Operation operation = multiWorldEdit
                    .create(schematicClipboard, editSession, world)
                    .to(position.getX(), position.getY(), position.getZ())
                    .ignoreAirBlocks(true)
                    .build();

            Operations.completeLegacy(operation);
        } catch (IOException | MaxChangedBlocksException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void clear(Vector minPoint, Vector maxPoint, World world) {
        org.bukkit.World bukkitWorld = BukkitConversionHelper.toWorld(world);
        for (int x = (int) minPoint.x(); x < maxPoint.x(); x++) {
            for (int y = (int) minPoint.y(); y < maxPoint.y(); y++) {
                for (int z = (int) minPoint.z(); z < maxPoint.z(); z++) {
                    bukkitWorld.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }
}
