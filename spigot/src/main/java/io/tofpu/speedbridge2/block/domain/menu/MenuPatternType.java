package io.tofpu.speedbridge2.block.domain.menu;

import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.mask.RecipeMask;

import java.util.Collection;
import java.util.Map;

public enum MenuPatternType {
    BINARY {
        @Override
        public Mask createMask(Menu menu, Collection<String> patterns, Map<Character, ItemStack> items) {
            if (patterns.isEmpty()) {
                throw new IllegalArgumentException("Patterns collection cannot be empty for BINARY pattern type");
            }
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Items map cannot be empty for BINARY pattern type");
            }

            BinaryMask.BinaryMaskBuilder builder = BinaryMask.builder(menu);
            patterns.forEach(builder::pattern);

            if (items.size() > 1) {
                throw new IllegalArgumentException("BINARY pattern type requires exactly one item in the map");
            } else {
                ItemStack patternItem = items.values().iterator().next();
                builder.item(patternItem);
            }

            return builder.build();
        }
    },
    RECIPE {
        @Override
        public Mask createMask(Menu menu, Collection<String> patterns, Map<Character, ItemStack> items) {
            if (patterns.isEmpty()) {
                throw new IllegalArgumentException("Patterns collection cannot be empty for RECIPE pattern type");
            }
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Items map cannot be empty for RECIPE pattern type");
            }

            RecipeMask.RecipeMaskBuilder builder = RecipeMask.builder(menu);
            patterns.forEach(builder::pattern);

            items.forEach((character, itemStack) -> {
                if (itemStack == null) {
                    throw new IllegalArgumentException("ItemStack for character '" + character + "' cannot be null");
                }
                builder.item(character, itemStack);
            });

            return builder.build();
        }
    };

    public abstract Mask createMask(Menu menu, Collection<String> patterns, Map<Character, ItemStack> items);

    public void apply(Menu menu, Collection<String> patterns, Map<Character, ItemStack> items) {
        Mask mask = createMask(menu, patterns, items);
        mask.apply(menu);
    }
}
