package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.game.arena.GameArenaManager;
import io.tofpu.speedbridge2.game.exception.GameException;
import io.tofpu.speedbridge2.game.exception.NonExistantWorldException;
import io.tofpu.speedbridge2.game.schematic.DefaultSchematicFetcher;
import io.tofpu.speedbridge2.game.schematic.SchematicFetcher;
import io.tofpu.speedbridge2.game.schematic.SchematicFetcherCached;
import io.tofpu.speedbridge2.island.SimpleIsland;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.bukkit.World;

import java.util.UUID;

// todo:
//  - Update the listener/bridge player
//    - Either remove gamePlayer instance & methods from BridgePlayer, and use GameService
//    - Or, update BridgePlayer to accept the new factored methods
public class GameService {
    private final GameRegistry<UUID, GameSession> gameRegistry;
    private final GameInitiate gameInitiate;

    public GameService(World world) throws NonExistantWorldException {
        this(new GameRegistry<>(), new DefaultSchematicFetcher(), new GameArenaManager(world));
    }

    public GameService(GameRegistry<UUID, GameSession> gameRegistry, SchematicFetcher schematicFetcher, final GameArenaManager gameArenaManager) {
        this.gameRegistry = gameRegistry;
        this.gameInitiate = new GameInitiate(new SchematicFetcherCached(schematicFetcher), gameArenaManager);
    }

    public GameSession start(final SimpleIsland island, final BridgePlayer player) {
        // retrieve clipboard
        // retrieve land
        // start game
        // store game
        // stop game
        try {
            final GameFactory gameFactory = this.gameInitiate.initiate(island, DefaultGameStrategy::new);
            final GameSession gameSession = gameFactory.start(player);

            this.gameRegistry.store(player.getPlayerUid(), gameSession);
            return gameSession;
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPlaying(final UUID playerId) {
        return gameRegistry.getBy(playerId) != null;
    }

    public void stop(final UUID playerId) {
        GameSession gameSession = this.gameRegistry.getBy(playerId);
        if (gameSession == null) {
            return;
        }

        gameSession.stop();
    }
}