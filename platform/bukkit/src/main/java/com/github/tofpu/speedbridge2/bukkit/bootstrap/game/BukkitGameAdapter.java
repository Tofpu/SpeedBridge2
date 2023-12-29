package com.github.tofpu.speedbridge2.bukkit.bootstrap.game;

import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.bukkit.helper.BukkitConversionHelper;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BukkitGameAdapter implements PlatformGameAdapter {
    @Override
    public void prepareGame(IslandGame game, IslandGamePlayer player) {
        preparePlayer(game, player);
    }

    @Override
    public void cleanGame(IslandGame game, IslandGamePlayer player) {
        clearGame(game);
    }

    @Override
    public void resetGame(IslandGame game, IslandGamePlayer player) {
        clearGame(game);
        preparePlayer(game, player);
    }

    private static void clearGame(IslandGame game) {
        IslandGameData gameData = game.data();
        gameData.blockPlacements().forEach(position -> {
            Location location = BukkitConversionHelper.toLocation(position);
            location.getWorld().getBlockAt(location).setType(Material.AIR);
        });
        gameData.resetState();
    }

    private static void preparePlayer(IslandGame game, IslandGamePlayer player) {
        UUID id = player.id();
        Player bukkitPlayer = Bukkit.getPlayer(id);

        player.getPlayer().teleport(game.data().getLand().getIslandLocation());

        bukkitPlayer.getInventory().clear();
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);
        bukkitPlayer.getActivePotionEffects().forEach(potionEffect -> bukkitPlayer.removePotionEffect(potionEffect.getType()));

        bukkitPlayer.setHealth(bukkitPlayer.getMaxHealth());
        bukkitPlayer.setFoodLevel(20);

        // todo: add an option allowing players to choose their own material type
        bukkitPlayer.getInventory().setItem(0, new ItemStack(Material.WOOL, 64));
    }
}
