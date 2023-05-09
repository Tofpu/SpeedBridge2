package io.tofpu.speedbridge2.game.arena;

import io.tofpu.speedbridge2.game.IslandGameSession;
import io.tofpu.speedbridge2.game.arena.land.LandSchematic;
import io.tofpu.speedbridge2.game.exception.NonExistantWorldException;
import io.tofpu.speedbridge2.game.object.FakeWorld;
import io.tofpu.speedbridge2.game.generic.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ArenaManagerTest {
    @Test
    void basic() throws NonExistantWorldException {
        final World world = new FakeWorld();
        final GameArenaManager gameArenaManager = new StandardArenaManager(world, 10);

        UUID playerId = UUID.randomUUID();
        int islandSlot = 1;

        gameArenaManager.reserveArea(new IslandGameSession(playerId, islandSlot), LandSchematic.empty());
        Assertions.assertTrue(gameArenaManager.isReserved(playerId));

        gameArenaManager.unreserveArea(playerId);
        Assertions.assertFalse(gameArenaManager.isReserved(playerId));

        // repeating the reserving process, in-case there were nasty edge-cases that I may have missed
        gameArenaManager.reserveArea(new IslandGameSession(playerId, islandSlot), LandSchematic.empty());
        Assertions.assertTrue(gameArenaManager.isReserved(playerId));

        gameArenaManager.unreserveArea(playerId);
        Assertions.assertFalse(gameArenaManager.isReserved(playerId));
    }
}
