package io.tofpu.speedbridge2.game.arena;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import io.tofpu.multiworldedit.EditSessionWrapper;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.Arrays;

public class LandArea {
    private final World world;
    private final int slot;
    private final Clipboard clipboard;
    private final double[] positions;
    private boolean reserved = false;

    public LandArea(World world, int slot, Clipboard clipboard, double[] positions) {
        this.world = world;
        this.slot = slot;
        this.clipboard = clipboard;
        this.positions = positions;
    }

    public double getX() {
        return this.positions[0];
    }

    public double getY() {
        return this.positions[1];
    }

    public double getZ() {
        return this.positions[2];
    }

    public int getWidth() {
        return this.clipboard.getRegion().getWidth();
    }

    public int getSlot() {
        return slot;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void generate(World world) {
        BridgeUtil.debug("Generating plot at " + Arrays.toString(this.positions) + " for " +
                "island " + this.slot);

        final BukkitWorld bukkitWorld = new BukkitWorld(world);

        try (final EditSessionWrapper editSessionWrapper = MultiWorldEditAPI.getMultiWorldEdit()
                .create(bukkitWorld, -1)) {
            final EditSession editSession = editSessionWrapper.to();

            final Operation operation = MultiWorldEditAPI.getMultiWorldEdit()
                    .create(this.clipboard, editSession, bukkitWorld)
                    .to((int) getX(), (int) getY(), (int) getZ())
                    .ignoreAirBlocks(true)
                    .build();

            Operations.completeLegacy(operation);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }

    public Location getIslandLocation() {
        return new Location(world, getX(), getY(), getZ(), 0f, 0f);
    }
}
