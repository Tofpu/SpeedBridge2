package com.github.tofpu.speedbridge2;

import java.io.File;

public interface LogicBootStrap {
    ArenaAdapter arenaAdapter();
    PlatformGameAdapter gameAdapter();
    File schematicFolder();
}
