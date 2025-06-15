package io.tofpu.speedbridge2.environment.infra;

import io.tofpu.speedbridge2.environment.infra.generator.EmptyChunkGenerator;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class EnvironmentHandler {
    protected static final String WORLD_NAME = "speedbridge2";
    /**
     * The directory where the worlds are stored.
     */
    private final File worldContainer;

    /**
     * The directory where our world is stored.
     */
    private File worldDirectory;

    private World world;

    private boolean isSetup = false;

    public EnvironmentHandler(File worldContainer) {
        this.worldContainer = worldContainer;
    }

    public void setupEnvironment() {
        if (isSetup) {
            return;
        }

        this.worldDirectory = new File(this.worldContainer, WORLD_NAME);
        if (this.worldDirectory.exists()) {
            resetEnvironment();
        }

        this.world = WorldCreator.name(WORLD_NAME)
                .generator(EmptyChunkGenerator.INSTANCE)
                .createWorld();

        setupWorldSettings();
        isSetup = true;
    }

    private void resetEnvironment() {
        if (worldDirectory == null || !worldDirectory.exists()) {
            return;
        }
        try {
            // delete the world outright
            FileUtils.forceDelete(worldDirectory);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Could not delete the %s world!", WORLD_NAME), e);
        }
    }

    private void setupWorldSettings() {
        world.setFullTime(1000);
        world.setWeatherDuration(0);
        world.setStorm(false);
        world.setThundering(false);
        world.setPVP(false);
        world.setAutoSave(false);
        world.setMonsterSpawnLimit(0);

        world.setGameRuleValue("doDaylightCycle", "false");
    }

    public World getWorld() {
        requireTheEnvironmentIsPrepared();
        return world;
    }

    private void requireTheEnvironmentIsPrepared() {
        if (!isSetup) throw new IllegalStateException("The environment is not prepared!");
    }
}
