package com.github.tofpu.speedbridge2.util;

import org.jetbrains.annotations.Nullable;

import static java.util.Locale.US;

public class ProgramCorrectness {

    /**
     * Ensures that the state expression is true.
     */
    public static void requireState(boolean expression, String template, @Nullable Object... args) {
        if (!expression) {
            throw new IllegalStateException(String.format(US, template, args));
        }
    }

    /**
     * Ensures that the argument expression is true.
     */
    public static void requireArgument(boolean expression, String template,
        @Nullable Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(US, template, args));
        }
    }
}
