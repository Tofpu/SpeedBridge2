package io.tofpu.speedbridge2.model.player.object.setup;

public interface SetupMeta {
    /**
     * Returns true if the current state is in the setup phase
     *
     * @return A boolean value.
     */
    boolean isInSetup();
    /**
     * Toggle the setup mode of the program
     */
    void toggleSetup();
    /**
     * Reset the setup to the default values
     */
    void resetSetup();
}
