package io.tofpu.speedbridge2.island.domain;

public interface ValidatableIsland extends IslandDescriptor {
    UnresolvedReason[] invalidReasons();

    default boolean isValid() {
        return invalidReasons().length == 0;
    }
}
