package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.island.SimpleIsland;

public class GameSession {
    private final GamePlayer player;
    private final SimpleIsland island;
    private final GameStrategy gameStrategy;

    public GameSession(GameFactory gameFactory) {
        this.player = gameFactory.getPlayer();
        this.island = gameFactory.getIsland();
        this.gameStrategy = gameFactory.getGameStrategy();
    }

    public void resetGame(final boolean notify) {
        this.gameStrategy.reset(notify);
    }

    public void stop() {
        this.gameStrategy.stop();
    }

    public void resetGame() {
        resetGame(true);
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public SimpleIsland getIsland() {
        return island;
    }
}
