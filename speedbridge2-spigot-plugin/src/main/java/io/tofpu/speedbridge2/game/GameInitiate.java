package io.tofpu.speedbridge2.game;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import io.tofpu.speedbridge2.game.arena.GameArenaManager;
import io.tofpu.speedbridge2.game.arena.LandArea;
import io.tofpu.speedbridge2.game.exception.GameException;
import io.tofpu.speedbridge2.game.exception.UnableToFindClipboardException;
import io.tofpu.speedbridge2.game.schematic.SchematicFetcher;
import io.tofpu.speedbridge2.game.schematic.SchematicFetcherCached;
import io.tofpu.speedbridge2.island.SimpleIsland;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class GameInitiate {
    private final SchematicFetcher schematicFetcher;
    private final GameArenaManager arenaManager;

    public GameInitiate(SchematicFetcher schematicFetcher, GameArenaManager arenaManager) {
        this.schematicFetcher = new SchematicFetcherCached(schematicFetcher);
        this.arenaManager = arenaManager;
    }

    public GameFactory initiate(final SimpleIsland island, Function<GameFactory, GameStrategy> gameStrategyFunction) throws GameException {
        final Clipboard clipboard = getClipboard(island);
        final LandArea landArea = this.arenaManager.reserveArea(island.getSlot(), clipboard);

        return new GameFactory(island, landArea, gameStrategyFunction);
    }

    @NotNull
    private Clipboard getClipboard(SimpleIsland island) throws GameException {
        String schematicName = island.getIslandSchematicInfo().getSchematicName();
        return getClipboard(schematicName, () -> new UnableToFindClipboardException(island.getSlot(), schematicName));
    }

    private Clipboard getClipboard(String schematicName, Supplier<Exception> elseThrowException) throws GameException {
        Clipboard clipboardBy = schematicFetcher.findClipboardBy(schematicName);
        if (clipboardBy == null) {
            throw new GameException(elseThrowException.get());
        }
        return clipboardBy;
    }
}
