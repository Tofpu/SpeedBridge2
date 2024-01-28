package com.github.tofpu.speedbridge2.bukkit.command.util;

import revxrsal.commands.core.CommandPath;

import java.util.Objects;

public class CommandUtil {
    public static boolean isSameRoot(CommandPath that, CommandPath other) {
        return Objects.equals(that.getFirst(), other.getFirst());
    }
}
