package io.tofpu.speedbridge2.game;

public abstract class GameStrategy {
    protected final GameFactory gameFactory;

    protected GameStrategy(GameFactory gameFactory) {
        this.gameFactory = gameFactory;
    }

    public abstract void start();
    public abstract void reset(final boolean notify);
    public abstract void stop();
}
