package io.tofpu.speedbridge2.domain.island.object.umbrella;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.domain.common.util.UmbrellaUtil;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.umbrella.UmbrellaAPI;
import io.tofpu.umbrella.domain.Umbrella;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import io.tofpu.umbrella.domain.item.factory.UmbrellaItemFactory;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class GameIslandUmbrella {
    private final Umbrella umbrella;

    public GameIslandUmbrella(final GameIsland gameIsland) {
        this.umbrella = UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaFactory()
                .create("island");

        final UmbrellaItemFactory umbrellaItemFactory = UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaItemFactory();

        final ItemStack resetItem = UmbrellaUtil.create(XMaterial.RED_DYE, "Reset",
                "reset the game!");
        umbrellaItemFactory.create(umbrella, "reset", resetItem, 7,
                new AbstractItemAction() {
            @Override
            public void trigger(final Umbrella umbrella, final PlayerInteractEvent event) {
                gameIsland.resetGame();
            }
        });

        final ItemStack leaveItem = UmbrellaUtil.create(XMaterial.RED_BED, "Leave",
                "leave the game!");
        umbrellaItemFactory.create(umbrella, "leave", leaveItem, 8,
                new AbstractItemAction() {
            @Override
            public void trigger(final Umbrella umbrella, final PlayerInteractEvent event) {
                gameIsland.stopGame();
            }
        });
    }

    public Umbrella getUmbrella() {
        return umbrella;
    }
}
