package io.tofpu.speedbridge2.command.subcommand.debug;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import io.tofpu.multiworldedit.EditSessionWrapper;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;

import java.io.IOException;

public class DestroyableLand extends IslandLand {
    private EditSession editSession;

    public DestroyableLand(IslandLand islandLand) {
        super(islandLand.getIsland(), islandLand.getWorld(), islandLand.getPositions());
    }

    @Override
    public void generatePlot() throws WorldEditException {
        // TODO: Make this generation operation async
        BridgeUtil.debug("Generating plot at " + getPlotLocation().toString() + " for " +
                         "island " + getIsland().getSlot());

        final BukkitWorld bukkitWorld = new BukkitWorld(getWorld());

        try (final EditSessionWrapper editSessionWrapper = MultiWorldEditAPI.getMultiWorldEdit()
                .create(bukkitWorld, -1)) {
            final Clipboard schematicClipboard = getIsland().getSchematicClipboard();
            this.editSession = editSessionWrapper.to();

            final Operation operation = MultiWorldEditAPI.getMultiWorldEdit()
                    .create(schematicClipboard, editSession, bukkitWorld)
                    .to((int) getX(), (int) getY(), (int) getZ())
                    .ignoreAirBlocks(true)
                    .build();

            Operations.completeLegacy(operation);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void destroy() {
        // TODO: Make this generation operation async
        BridgeUtil.debug("Generating plot at " + getPlotLocation().toString() + " for " +
                         "island " + getIsland().getSlot());

        final BukkitWorld bukkitWorld = new BukkitWorld(getWorld());

        try (final EditSessionWrapper editSessionWrapper = MultiWorldEditAPI.getMultiWorldEdit()
                .create(bukkitWorld, -1)) {
            EditSession session = editSessionWrapper.to();
            this.editSession.undo(session);
//            session.undo(this.editSession);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        this.free();
    }
}
