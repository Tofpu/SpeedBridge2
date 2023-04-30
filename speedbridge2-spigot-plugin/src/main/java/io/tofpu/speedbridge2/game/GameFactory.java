package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.game.arena.LandArea;
import io.tofpu.speedbridge2.island.SimpleIsland;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;

import java.util.function.Function;

public class GameFactory {
    private final SimpleIsland island;
    private final LandArea landArea;
    private final GameStrategy gameStrategy;

    private GamePlayer player;
    private GameSession gameSession;

    public GameFactory(SimpleIsland island, LandArea landArea, Function<GameFactory, GameStrategy> gameStrategyFunction) {
        this.island = island;
        this.landArea = landArea;
        this.gameStrategy = gameStrategyFunction.apply(this);
    }

    public GameSession start(BridgePlayer player) {
        this.player = new GamePlayer(player);
        this.gameSession = new GameSession(this);

        gameStrategy.start();
        return gameSession;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public SimpleIsland getIsland() {
        return island;
    }

    public LandArea getLandArea() {
        return landArea;
    }

    public GameStrategy getGameStrategy() {
        return gameStrategy;
    }
}
