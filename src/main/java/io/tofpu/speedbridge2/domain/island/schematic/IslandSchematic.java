package io.tofpu.speedbridge2.domain.island.schematic;

import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.SchematicReader;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class IslandSchematic {
    private @Nullable String schematicName = "";
    private @Nullable Clipboard schematicClipboard;

    public IslandSchematic() {}

    protected boolean selectSchematic(final @NotNull String schematicName) {
        final LocalConfiguration configuration = WorldEdit.getInstance()
                .getConfiguration();
        final File directory = Bukkit.getPluginManager()
                .getPlugin("WorldEdit")
                .getDataFolder()
                .toPath()
                .resolve(configuration.saveDir)
                .toFile();

        BridgeUtil.debug("worldedit's directory: " + directory);
        final File file = directory.toPath().resolve(schematicName + ".schematic").toFile();

        if (file.exists()) {
            NBTInputStream nbtStream;
            try {
                nbtStream = new NBTInputStream(new GZIPInputStream(new FileInputStream(file)));
                final ClipboardReader reader = new SchematicReader(nbtStream);
                this.schematicClipboard = reader.read(null);

                BridgeUtil.debug("successfully set the island's schematic");
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.schematicName = schematicName;
        } else {
            BridgeUtil.debug(schematicName + " cannot be found as a schematic");
        }

        return file.exists();
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
