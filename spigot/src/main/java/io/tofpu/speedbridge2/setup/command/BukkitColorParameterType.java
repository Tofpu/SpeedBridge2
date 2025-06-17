package io.tofpu.speedbridge2.setup.command;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BukkitColorParameterType implements ParameterType<BukkitCommandActor, Color> {
    private final Map<String, Color> colorMap = new HashMap<>();

    public BukkitColorParameterType() {
        registerColorNames();
    }

    private void registerColorNames() {
        for (Field field : Color.class.getDeclaredFields()) {
            if (field.getType() == Color.class) {
                try {
                    Color color = (Color) field.get(null);
                    colorMap.put(field.getName().toLowerCase(), color);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access color field: " + field.getName(), e);
                }
            }
        }
    }

    @Override
    public Color parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> context) {
        String colorName = input.readString();
        Color color = colorMap.get(colorName.toLowerCase());
        if (color == null) {
            throw new IllegalArgumentException("Unknown color: " + colorName);
        }
        return color;
    }

    @Override
    public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
        return context -> colorMap.keySet().stream()
                .map(String::toLowerCase)
                .toList();
    }
}
