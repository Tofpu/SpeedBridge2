package io.tofpu.speedbridge2.model.island.schematic;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import io.tofpu.multiworldedit.ClipboardWrapper;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.object.Island;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

public class IslandSchematic {
    private static final String[] SCHEMATIC_TYPES = {"schematic", "schem"};

    private final Island island;
    private final File schematicdirectory;

    private @NotNull String schematicName = "";
    private @Nullable ClipboardWrapper schematicClipboard;

    public IslandSchematic(final Island island) {
        this.island = island;
        schematicdirectory = Bukkit.getPluginManager()
                .getPlugin("WorldEdit")
                .getDataFolder()
                .toPath()
                .resolve("schematics")
                .toFile();
    }

    public boolean selectSchematic(final @NotNull String schematicName) {
        BridgeUtil.debug(
                "Loading schematic '" + schematicName + "' for " + island.getSlot() +
                "...");
        BridgeUtil.debug("IslandSchematic#selectSchematic(): WorldEdit Directory: " +
                         schematicdirectory);

        final File file = findSchematicFile(schematicdirectory, schematicName);
        if (file != null && file.exists()) {
            this.schematicClipboard = MultiWorldEditAPI.getMultiWorldEdit()
                    .read(file);

            BridgeUtil.debug(
                    "IslandSchematic#selectSchematic(): Successfully loaded schematic: " +
                    schematicName);

            this.schematicName = schematicName;
            return true;
        }

        BridgeUtil.debug("IslandSchematic#selectSchematic(): Failed to load schematic: " +
                         schematicName);
        return false;
    }

    private File findSchematicFile(final @NotNull File directory, final @NotNull String name) {
        final Path directoryPath = directory.toPath();

        File file = null;
        for (final String schematicType : SCHEMATIC_TYPES) {
            file = directoryPath.resolve(name + "." + schematicType)
                    .toFile();

            if (file.exists()) {
                break;
            }
        }
        return file;
    }

    public @NotNull String getSchematicName() {
        return schematicName;
    }

    public @Nullable Clipboard getSchematicClipboard() {
        if (this.schematicClipboard == null) {
            return null;
        }
        return this.schematicClipboard.to();
    }

    public @Nullable ClipboardWrapper getSchematicClipboardWrapper() {
        return this.schematicClipboard;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IslandSchematic{");
        sb.append("islandSlot=")
                .append(island.getSlot());
        sb.append(", schematicdirectory=")
                .append(schematicdirectory);
        sb.append(", schematicName='")
                .append(schematicName)
                .append('\'');
        sb.append(", schematicClipboard=")
                .append(schematicClipboard);
        sb.append('}');
        return sb.toString();
    }
}
