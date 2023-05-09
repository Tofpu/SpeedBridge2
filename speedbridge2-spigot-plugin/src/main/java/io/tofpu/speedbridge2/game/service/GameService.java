package io.tofpu.speedbridge2.game.service;

import com.github.tofpu.gameengine.GameEngine;
import io.tofpu.speedbridge2.game.IslandGameSession;
import io.tofpu.speedbridge2.game.IslandPlayerSession;
import io.tofpu.speedbridge2.game.arena.GameArenaManager;
import io.tofpu.speedbridge2.island.fake.FakeIslandService;

import java.util.UUID;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireState;

public class GameService {
    private final GameEngine<IslandGameSession, IslandPlayerSession> gameEngine;

    public GameService(final GameEngine<IslandGameSession, IslandPlayerSession> engine) {
        this.gameEngine = engine;
    }

    public GameService(final FakeIslandService islandService, final GameArenaManager gameArenaManager) {
        this(new BasicGameEngine(islandService, gameArenaManager).create());
    }

    public void start(final UUID playerId, final int slot) {
        this.gameEngine.begin(playerId, slot);
    }

    public void stop(final UUID playerId) {
        requireState(isPlaying(playerId), "Player %s is not in a game", playerId);
        this.gameEngine.end(playerId);
    }

    public boolean isPlaying(final UUID playerId) {
        return this.gameEngine.isPlaying(playerId);
    }
}
