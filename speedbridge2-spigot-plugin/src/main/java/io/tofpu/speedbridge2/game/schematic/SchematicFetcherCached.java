package io.tofpu.speedbridge2.game.schematic;

import com.sk89q.worldedit.extent.clipboard.Clipboard;

import java.util.HashMap;
import java.util.Map;

public class SchematicFetcherCached implements SchematicFetcher {
    private final Map<String, Clipboard> cacheMap = new HashMap<>();
    private final SchematicFetcher delegate;

    public SchematicFetcherCached(SchematicFetcher delegate) {
        this.delegate = delegate;
    }

    public Clipboard getCachedClipboard(String schematicName) {
        return cacheMap.get(schematicName);
    }

    @Override
    public Clipboard findClipboardBy(String schematicName) {
        Clipboard cachedClipboard = getCachedClipboard(schematicName);
        if (cachedClipboard != null) {
            return cachedClipboard;
        }
        return storeCacheAndReturn(schematicName, getNonCachedClipboard(schematicName));
    }

    private Clipboard storeCacheAndReturn(String schematicName, Clipboard nonCachedClipboard) {
        if (nonCachedClipboard != null) {
            this.cacheMap.put(schematicName, nonCachedClipboard);
        }
        return nonCachedClipboard;
    }

    private Clipboard getNonCachedClipboard(String schematicName) {
        return delegate.findClipboardBy(schematicName);
    }
}
