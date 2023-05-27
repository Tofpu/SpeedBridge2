package com.github.tofpu.speedbridge2.util;

import org.bukkit.Bukkit;

public class SpigotTestUtil {

    public static void clearBukkitServerInstance() {
        ReflectionUtil.setStaticField(Bukkit.class, "server", null);
    }
}
