package io.tofpu.speedbridge2.model.island.setup.umbrella;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.model.common.util.UmbrellaUtil;
import io.tofpu.umbrella.UmbrellaAPI;
import io.tofpu.umbrella.domain.Umbrella;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import io.tofpu.umbrella.domain.item.factory.UmbrellaItemFactory;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class IslandSetupUmbrella {
    private final Umbrella umbrella;

    public IslandSetupUmbrella() {
        this.umbrella = UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaFactory()
                .create("island_setup");

        final UmbrellaItemFactory umbrellaItemFactory = UmbrellaAPI.getInstance()
                .getUmbrellaService()
                .getUmbrellaItemFactory();

        final ItemStack finishItem = UmbrellaUtil.create(XMaterial.GREEN_DYE,
                "Complete the " + "Island Setup", "complete the island setup!");
        umbrellaItemFactory.create(umbrella, "finish", finishItem, 3, new AbstractItemAction() {
            @Override
            public void trigger(final Umbrella umbrella, final PlayerInteractEvent event) {
                event.getPlayer()
                        .performCommand("sb setup finish");
            }
        });

        final ItemStack setSpawnItem = UmbrellaUtil.create(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE,
                "Set the Island Spawnpoint", "set the island spawn-point!");
        umbrellaItemFactory.create(umbrella, "set_spawn", setSpawnItem, 4, new AbstractItemAction() {
            @Override
            public void trigger(final Umbrella umbrella, final PlayerInteractEvent event) {
                event.getPlayer()
                        .performCommand("sb setup setspawn");
            }
        });

        final ItemStack cancelItem = UmbrellaUtil.create(XMaterial.RED_DYE,
                "Cancel the " + "Island Setup", "cancel the island setup!");
        umbrellaItemFactory.create(umbrella, "cancel", cancelItem, 5, new AbstractItemAction() {
            @Override
            public void trigger(final Umbrella umbrella, final PlayerInteractEvent event) {
                event.getPlayer()
                        .performCommand("sb setup cancel");
            }
        });
    }

    public Umbrella getUmbrella() {
        return umbrella;
    }
}
