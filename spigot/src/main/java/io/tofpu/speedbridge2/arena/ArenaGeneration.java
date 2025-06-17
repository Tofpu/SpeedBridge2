package io.tofpu.speedbridge2.arena;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.EditSessionWrapper;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.multiworldedit.VectorWrapper;
import io.tofpu.speedbridge2.util.Position;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.IOException;

public class ArenaGeneration {
    private final World world;
    private final ClipboardWrapper clipboard;
    private final Position position;

    public ArenaGeneration(World world, ClipboardWrapper clipboard, Position position) {
        this.world = world;
        this.clipboard = clipboard;
        this.position = position;
    }

    /**
     * Generates the arena in the world using the provided {@link Clipboard} and {@link Position} data.
     */
    public void generate() {
        final BukkitWorld bukkitWorld = new BukkitWorld(world);

        try (final EditSessionWrapper editSessionWrapper =
                MultiWorldEditAPI.getMultiWorldEdit().create(bukkitWorld, -1)) {
            final EditSession editSession = editSessionWrapper.to();

            final Operation operation = MultiWorldEditAPI.getMultiWorldEdit()
                    .create(clipboard.to(), editSession, bukkitWorld)
                    .to(position.x(), position.y(), position.z())
                    .ignoreAirBlocks(true)
                    .build();

            Operations.completeLegacy(operation);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Destroys the arena in the world using the provided {@link Clipboard} and {@link Position} data.
     */
    public void destroy(CuboidRegion arenaRegion) {
        final ClipboardWrapper clipboardWrapper =
                MultiWorldEditAPI.getMultiWorldEdit().create(clipboard.to());

        final VectorWrapper minimumPoint = arenaRegion.minVector();
        final VectorWrapper maximumPoint = arenaRegion.maxVector();

        final int offset = 1;

        // resetting the blocks
        for (int x = (int) minimumPoint.getX() - offset; x < maximumPoint.getX() + offset; x++) {
            for (int y = (int) minimumPoint.getY() - offset; y < maximumPoint.getY() + offset; y++) {
                for (int z = (int) minimumPoint.getZ() - offset; z < maximumPoint.getZ() + offset; z++) {
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }
}
