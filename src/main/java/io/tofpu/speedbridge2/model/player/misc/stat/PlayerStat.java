package io.tofpu.speedbridge2.model.player.misc.stat;

import java.util.UUID;

public interface PlayerStat {
    /**
     * Get the owner of the object
     *
     * @return The owner of the UUID.
     */
    UUID getOwner();

    /**
     * Returns the key of the current element
     *
     * @return The key of the current node.
     */
    String getKey();
    /**
     * The function returns a string
     *
     * @return The value of the question.
     */
    String getValue();

    /**
     * Increment the value of the global variable `i`
     */
    void increment();
}
