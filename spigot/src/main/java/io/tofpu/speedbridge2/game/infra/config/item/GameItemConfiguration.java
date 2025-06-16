package io.tofpu.speedbridge2.game.infra.config.item;

import io.tofpu.speedbridge2.game.infra.config.item.serializer.ItemMetaOptionsSerializer;
import io.tofpu.speedbridge2.game.infra.config.item.serializer.ItemStackSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfSerialisers;
import space.arim.dazzleconf.annote.SubSection;

import java.util.Map;

import static io.tofpu.speedbridge2.game.infra.config.GameConfigDefaults.Items.leaveGameItem;
import static io.tofpu.speedbridge2.game.infra.config.GameConfigDefaults.Items.resetGameItem;
import static org.immutables.value.Value.Immutable;

@ConfSerialisers(value = {ItemStackSerializer.class, ItemMetaOptionsSerializer.class})
@Immutable
public interface GameItemConfiguration {
    static Builder builder() {
        return new Builder();
    }

    static GameItemConfiguration of(Item leaveGame, Item resetGame) {
        return ImmutableGameItemConfiguration.of(
                Map.of(
                        ItemType.LEAVE_GAME, leaveGame,
                        ItemType.RESET_GAME, resetGame
                )
        );
    }

    enum ItemType {
        LEAVE_GAME,
        RESET_GAME
    }

    default Item leaveGame() {
        return items().get(ItemType.LEAVE_GAME);
    }

    default Item resetGame() {
        return items().get(ItemType.RESET_GAME);
    }

    @ConfDefault.DefaultObject("defaultItems")
    Map<ItemType, @SubSection Item> items();

    static Map<ItemType, GameItemConfiguration.Item> defaultItems() {
        return Map.of(
                ItemType.LEAVE_GAME, leaveGameItem(),
                ItemType.RESET_GAME, resetGameItem()
        );
    }

    @Immutable
    interface Item {
        static Builder builder() {
            return new Builder();
        }

        static Item of(ItemStack item, int slot) {
            return ImmutableItem.of(item, slot);
        }

        @ConfDefault.DefaultObject("defaultItemStack")
        ItemStack item();

        static ItemStack defaultItemStack() {
            return new ItemStack(Material.AIR);
        }

        @ConfDefault.DefaultInteger(0)
        int slot();

        class Builder extends ImmutableItem.Builder {}
    }

    class Builder extends ImmutableGameItemConfiguration.Builder {}
}
