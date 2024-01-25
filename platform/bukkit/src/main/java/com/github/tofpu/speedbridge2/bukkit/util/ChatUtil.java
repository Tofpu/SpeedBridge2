package com.github.tofpu.speedbridge2.bukkit.util;

import org.bukkit.ChatColor;

public class ChatUtil {
    public static String colorize(String content) {
        return ChatColor.translateAlternateColorCodes('&', content);
    }
}
