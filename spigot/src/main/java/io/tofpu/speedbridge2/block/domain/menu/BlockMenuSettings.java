package io.tofpu.speedbridge2.block.domain.menu;

import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.Mask;

import java.util.Collection;
import java.util.Map;

public record BlockMenuSettings(
        String title,
        int rows,
        Pattern pattern
) {
    public record Pattern(
            MenuPatternType type,
            Collection<String> patterns,
            Map<Character, ItemStack> replacements
    ) {
        public void apply(Menu menu) {
            type.apply(menu, patterns, replacements);
        }

        public Mask createMask(Menu menu) {
            return type.createMask(menu, patterns, replacements);
        }
    }
}
