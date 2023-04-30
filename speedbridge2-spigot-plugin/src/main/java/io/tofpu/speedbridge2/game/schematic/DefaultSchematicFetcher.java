package io.tofpu.speedbridge2.game.schematic;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import io.tofpu.multiworldedit.MultiWorldEditAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public class DefaultSchematicFetcher implements SchematicFetcher {
    private static final String[] SCHEMATIC_TYPES = {"schematic", "schem"};
    private final File schematicDirectory;

    public DefaultSchematicFetcher() {
        this.schematicDirectory = Bukkit.getPluginManager()
                .getPlugin("WorldEdit")
                .getDataFolder()
                .toPath()
                .resolve("schematics")
                .toFile();
    }

    @Override
    public Clipboard findClipboardBy(final String schematicName) {
        final File file = findSchematicFile(schematicName);
        if (file == null || !file.exists()) {
            return null;
        }

        return MultiWorldEditAPI.getMultiWorldEdit()
                .read(file).to();
    }

    private File findSchematicFile(final @NotNull String name) {
        final Path directoryPath = schematicDirectory.toPath();

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
}
