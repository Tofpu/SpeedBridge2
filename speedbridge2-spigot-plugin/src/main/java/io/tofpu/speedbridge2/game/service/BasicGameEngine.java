package io.tofpu.speedbridge2.game.service;

import com.github.tofpu.gameengine.AbstractGameEngine;
import com.github.tofpu.gameengine.GameSession;
import io.tofpu.speedbridge2.game.IslandGameSession;
import io.tofpu.speedbridge2.game.IslandPlayerSession;
import io.tofpu.speedbridge2.game.arena.GameArenaManager;
import io.tofpu.speedbridge2.island.fake.FakeIsland;
import io.tofpu.speedbridge2.island.fake.FakeIslandService;

import java.util.Arrays;
import java.util.UUID;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireArgument;
import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireState;

public class BasicGameEngine extends AbstractGameEngine<IslandGameSession, IslandPlayerSession> {
    private final GameRegistry<UUID, GameSession> gameRegistry = new GameRegistry<>();
    private final FakeIslandService islandService;
    private final GameArenaManager gameArenaManager;

    public BasicGameEngine(final FakeIslandService islandService, final GameArenaManager gameArenaManager) {
        this.islandService = islandService;
        this.gameArenaManager = gameArenaManager;
    }

    @Override
    public void onStart(IslandGameSession gameSession, IslandPlayerSession islandPlayerSession) {
        FakeIsland island = islandService.getUnsafe(gameSession.getSlot());
        requireState(island != null, "Unknown island slot of %s", gameSession.getSlot());

        gameArenaManager.reserveArea(gameSession, island.getSchematic());
        gameRegistry.store(islandPlayerSession.getId(), gameSession);
    }

    @Override
    public void onEnd(IslandGameSession gameSession, IslandPlayerSession islandPlayerSession) {
        int slot = gameSession.getSlot();

        requireState(gameArenaManager.isReserved(gameSession.getId()), "There is no reservation with island slot of %s. This should not have happened.", slot);
        gameArenaManager.unreserveArea(gameSession.getId());
        gameRegistry.remove(islandPlayerSession.getId());
    }

    @Override
    public IslandPlayerSession playerSession(UUID uuid, Object... objects) {
        return new IslandPlayerSession(uuid);
    }

    @Override
    public IslandGameSession gameSession(UUID uuid, Object... objects) {
        requireArgument(objects.length == 1, "Unknown arguments %s", Arrays.toString(objects));
        requireArgument(objects[0] instanceof Integer, "Must be an integer: %s", objects[0]);

        int islandSlot = (int) objects[0];
        FakeIsland island = islandService.getUnsafe(islandSlot);
        requireState(island != null, "Unknown island with slot %s", islandSlot);

        return new IslandGameSession(uuid, islandSlot);
    }
}
