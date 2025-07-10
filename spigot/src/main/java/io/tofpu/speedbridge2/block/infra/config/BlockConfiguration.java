package io.tofpu.speedbridge2.block.infra.config;

import io.tofpu.speedbridge2.block.domain.menu.MenuPatternType;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import space.arim.dazzleconf.annote.SubSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultInteger;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultObject;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultString;

public interface BlockConfiguration {
    @DefaultInteger(0)
    int blockSlot();

    @DefaultString("default")
    String defaultBlockId();

    static ItemStack defaultBlockItem() {
        return new ItemStack(Material.DIAMOND_BLOCK);
    }

    @SubSection
    BlockConfiguration.Menu menu();

    @DefaultObject("defaultBlocks")
    Collection<@SubSection Block> blocks();

    static Collection<Block> defaultBlocks() {
        return List.of(
                new Block() {
                    @Override
                    public String id() {
                        return "default";
                    }

                    @Override
                    public String viewPermission() {
                        return "";
                    }

                    @Override
                    public ItemStack item() {
                        ItemStack itemStack = new ItemStack(Material.STONE);
                        itemStack.editMeta(itemMeta -> {
                            itemMeta.displayName(text("Default Block", NamedTextColor.GRAY));
                            itemMeta.lore(List.of(
                                    text("This is the default block", NamedTextColor.GRAY),
                                    text("You can change it in the config", NamedTextColor.GRAY)
                            ));
                        });
                        return itemStack;
                    }
                },
                new Block() {
                    @Override
                    public String id() {
                        return "vip";
                    }

                    @Override
                    public String viewPermission() {
                        return "game.item.vip.view";
                    }

                    @Override
                    public ItemStack item() {
                        ItemStack itemStack = new ItemStack(Material.GOLD_BLOCK);
                        itemStack.editMeta(itemMeta -> {
                            itemMeta.displayName(text("VIP Block", NamedTextColor.GOLD));
                            itemMeta.lore(List.of(
                                    text("This block is for VIPs", NamedTextColor.GOLD),
                                    text("You can set it in the menu", NamedTextColor.GRAY)
                            ));
                        });
                        return itemStack;
                    }
                }
        );
    }

    interface Menu {
        @DefaultString("Block Menu")
        String title();

        @DefaultInteger(6)
        int rows();

        @SubSection
        Pattern pattern();

        interface Pattern {
            @DefaultString("RECIPE")
            MenuPatternType type();

            @DefaultObject("defaultPatterns")
            List<String> patterns();

            static List<String> defaultPatterns() {
                String top = "wr".repeat(4) + "w";
                String bottom = "rw".repeat(4) + "r";

                List<String> patterns = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    if (i == 0) {
                        patterns.add(top);
                        continue;
                    } else if (i == 5) {
                        patterns.add(bottom);
                        continue;
                    }
                    boolean odd = i % 2 == 1;
                    String letter = odd ? "r" : "w";
                    String middle = letter + "0".repeat(7) + letter;
                    patterns.add(middle);
                }
                return patterns;
            }

            @DefaultObject("defaultReplacementItems")
            Map<Character, ItemStack> replacementItems();

            static Map<Character, ItemStack> defaultReplacementItems() {
                return Map.of(
                        'w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE),
                        'r', new ItemStack(Material.RED_STAINED_GLASS_PANE)
                );
            }
        }
    }

    interface Block {
        @DefaultString("id")
        String id();

        @DefaultString("your.permission.view")
        @Nullable
        String viewPermission();

        @DefaultObject("defaultItemStack")
        ItemStack item();

        static ItemStack defaultItemStack() {
            return new ItemStack(Material.AIR);
        }
    }
}
