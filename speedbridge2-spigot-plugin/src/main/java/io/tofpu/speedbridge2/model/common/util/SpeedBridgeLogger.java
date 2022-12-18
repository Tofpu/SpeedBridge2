package io.tofpu.speedbridge2.model.common.util;

import org.bukkit.Bukkit;

public class SpeedBridgeLogger {
    private static final SpeedBridgeLogger INSTANCE = new SpeedBridgeLogger();

    public static SpeedBridgeLogger getInstance() {
        return INSTANCE;
    }

    public void debug(final String message) {
        Bukkit.getLogger()
                .info("[DEBUG] " + message);
    }
}
