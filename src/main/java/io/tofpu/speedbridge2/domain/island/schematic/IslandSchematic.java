package io.tofpu.speedbridge2.domain.island.schematic;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

public class IslandSchematic {
    private static final String[] SCHEMATIC_TYPES = {"schematic", "schem"};

    private @Nullable String schematicName = "";
    private @Nullable Clipboard schematicClipboard;

    public IslandSchematic() {}

    public boolean selectSchematic(final @NotNull String schematicName) {
        final File directory = Bukkit.getPluginManager()
                .getPlugin("WorldEdit")
                .getDataFolder()
                .toPath()
                .resolve("schematics")
                .toFile();

        BridgeUtil.debug("worldedit's directory: " + directory);

        final File file = findSchematicFile(directory, schematicName);
        if (file != null && file.exists()) {
            this.schematicClipboard = MultiWorldEditAPI.getMultiWorldEdit()
                    .read(file);

            BridgeUtil.debug("successfully set the island's schematic");

            this.schematicName = schematicName;
            return true;
        } else {
            BridgeUtil.debug(schematicName + " cannot be found as a schematic");
        }
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

    public @Nullable String getSchematicName() {
        return schematicName;
    }

    public @Nullable Clipboard getSchematicClipboard() {
        return this.schematicClipboard;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IslandSchematic{");
        sb.append("schematicName='").append(schematicName).append('\'');
        sb.append(", schematicClipboard=").append(schematicClipboard);
        sb.append('}');
        return sb.toString();
    }
}
