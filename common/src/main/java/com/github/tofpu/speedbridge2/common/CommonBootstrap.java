package com.github.tofpu.speedbridge2.common;

import java.io.File;

public interface CommonBootstrap {
    PlatformArenaAdapter arenaAdapter();
    PlatformGameAdapter gameAdapter();
    File schematicFolder();
}
