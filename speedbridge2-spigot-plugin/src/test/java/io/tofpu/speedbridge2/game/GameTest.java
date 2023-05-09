package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.game.exception.NonExistantWorldException;
import io.tofpu.speedbridge2.game.service.GameService;
import io.tofpu.speedbridge2.game.arena.StandardArenaManager;
import io.tofpu.speedbridge2.game.arena.land.LandSchematic;
import io.tofpu.speedbridge2.game.generic.Position;
import io.tofpu.speedbridge2.game.generic.World;
import io.tofpu.speedbridge2.game.object.FakeWorld;
import io.tofpu.speedbridge2.island.fake.FakeIslandService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    @Test
    public void test() throws NonExistantWorldException {
        final FakeIslandService fakeIslandService = new FakeIslandService();
        fakeIslandService.register(1, new LandSchematic(Position.zero(), Position.of(2, 2, 2), new HashMap<>()));

        final World world = new FakeWorld();
        final GameService gameService = new GameService(fakeIslandService, new StandardArenaManager(world, 10));

        UUID playerId = UUID.randomUUID();

        gameService.start(playerId, 1);
        assertTrue(gameService.isPlaying(playerId));

        gameService.stop(playerId);
        assertFalse(gameService.isPlaying(playerId));
    }
}
