package io.tofpu.speedbridge2.domain;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.util.io.file.FilenameException;
import org.bukkit.Bukkit;

import java.io.File;

public final class IslandSchematic {
    private String schematicName;
    private ClipboardFormat schematicClipboard;

    public IslandSchematic() {}
    public IslandSchematic(final String schematicName) {
        this.schematicName = schematicName;
        selectSchematic(schematicName);
    }

    public boolean selectSchematic(final String schematicName) {
        final LocalConfiguration configuration = WorldEdit.getInstance()
                .getConfiguration();
        final File directory = WorldEdit.getInstance().getWorkingDirectoryPath(configuration.saveDir).toFile();
//        final Actor actor = new BukkitPlayer(player);

        File file = null;
        try {
            file = WorldEdit.getInstance().getSafeOpenFile(null, directory, schematicName, BuiltInClipboardFormat.SPONGE_SCHEMATIC.getPrimaryFileExtension(),
                    ClipboardFormats.getFileExtensionArray());
        } catch (FilenameException e) {
            e.printStackTrace();
        }

        if (file != null) {
            this.schematicClipboard = ClipboardFormats.findByFile(file);
            Bukkit.getLogger().info("successfully set the island's schematic");
        } else {
            Bukkit.getLogger().info(schematicName + " cannot be found as a schematic");
        }

        return file != null && file.exists();
    }

    public String getSchematicName() {
        return schematicName;
    }

    public ClipboardFormat getSchematicClipboard() {
        return schematicClipboard;
    }
}
