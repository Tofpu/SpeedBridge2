package io.tofpu.speedbridge2.game.schematic;

import com.sk89q.worldedit.extent.clipboard.Clipboard;

public interface SchematicFetcher {
    Clipboard findClipboardBy(final String schematicName);
}
