package io.tofpu.speedbridge2.island.fake;

import io.tofpu.speedbridge2.game.arena.land.LandSchematic;

import java.util.HashMap;
import java.util.Map;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireState;

// todo: replace this with the original island service once it is in a usable state
public class FakeIslandService {
    private final Map<Integer, FakeIsland> islandMap = new HashMap<>();

    public void register(final int slot, LandSchematic schematic) {
        requireState(!isPresent(slot), "There is already an existing island with %s slot", slot);
        this.islandMap.put(slot, new FakeIsland(slot, schematic));
    }

    public boolean isPresent(int slot) {
        return islandMap.containsKey(slot);
    }

    public FakeIsland getUnsafe(int slot) {
        return islandMap.get(slot);
    }

    public FakeIsland get(int slot) {
        requireState(isPresent(slot), "There is no existing island with %s slot", slot);
        return getUnsafe(slot);
    }
}
