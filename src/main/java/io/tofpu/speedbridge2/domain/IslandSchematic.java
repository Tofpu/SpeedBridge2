package io.tofpu.speedbridge2.domain;

import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.SchematicReader;
import com.sk89q.worldedit.world.registry.WorldData;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public final class IslandSchematic {
    private String schematicName;
    private Clipboard schematicClipboard;

    public IslandSchematic() {}
    public IslandSchematic(final String schematicName) {
        if (selectSchematic(schematicName)) {
            this.schematicName = schematicName;
        }
    }

    public boolean selectSchematic(final String schematicName) {
        final LocalConfiguration configuration = WorldEdit.getInstance()
                .getConfiguration();
        final File directory = Bukkit.getPluginManager()
                .getPlugin("WorldEdit")
                .getDataFolder()
                .toPath()
                .resolve(configuration.saveDir)
                .toFile();

        System.out.println("worldedit's directory: " + directory);
        final File file = directory.toPath().resolve(schematicName + ".schem").toFile();

        if (file.exists()) {
            NBTInputStream nbtStream;
            try {
                nbtStream = new NBTInputStream(new GZIPInputStream(new FileInputStream(file)));
                final ClipboardReader reader = new SchematicReader(nbtStream);
                this.schematicClipboard = reader.read(null);

                Bukkit.getLogger().info("successfully set the island's schematic");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getLogger().info(schematicName + " cannot be found as a schematic");
        }

        return file.exists();
    }

    public String getSchematicName() {
        return schematicName;
    }

    public Clipboard getSchematicClipboard() {
        return schematicClipboard;
    }
}
