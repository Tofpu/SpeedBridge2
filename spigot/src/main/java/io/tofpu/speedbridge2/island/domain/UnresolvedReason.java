package io.tofpu.speedbridge2.island.domain;

public enum UnresolvedReason {
    MISSING_GROUP,
    MISSING_SCHEMATIC;

    public static final UnresolvedReason[] EMPTY = new UnresolvedReason[0];
}
