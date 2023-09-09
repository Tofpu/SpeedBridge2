package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.game.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.object.World;

public interface ArenaAdapter {
    World gameWorld();
    ClipboardPaster clipboardPaster();
}
