package com.github.tofpu.speedbridge2.bootstrap.game;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.adapter.SpeedBridgeAdapter;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class BukkitGameAdapter implements GameAdapter {
    private final OngoingGameRegistry gameRegistry = new OngoingGameRegistry();
    private IslandGameHandler gameHandler;
    private Plugin plugin;

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    private void register() {
        if (gameHandler == null) {
            throw new IllegalStateException("IslandGameHandler must be provided to register the game listener!");
        }
        Bukkit.getPluginManager().registerEvents(new GameListener(gameHandler, gameRegistry), plugin);
    }

    @Override
    public void prepareGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
        if (this.gameHandler == null) {
            this.gameHandler = gameHandler;
            register();
        }

        gameRegistry.add(player.id(), game);
        preparePlayer(game, player);
    }

    @Override
    public void cleanGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
        clearGame(game);
        gameRegistry.remove(player.id());
    }

    @Override
    public void resetGame(IslandGameHandler gameHandler, IslandGame game, IslandGamePlayer player) {
        clearGame(game);
        preparePlayer(game, player);
    }

    private static void clearGame(IslandGame game) {
        game.blockPlacements().forEach(position -> {
            Location location = SpeedBridgeAdapter.toLocation(position);
            location.getWorld().getBlockAt(location).setType(Material.AIR);
        });
        game.resetState();
    }

    private static void preparePlayer(IslandGame game, IslandGamePlayer player) {
        UUID id = player.id();
        Player bukkitPlayer = Bukkit.getPlayer(id);

        player.getPlayer().teleport(game.getLand().getIslandLocation());

        bukkitPlayer.getInventory().clear();
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);
        bukkitPlayer.getActivePotionEffects().forEach(potionEffect -> bukkitPlayer.removePotionEffect(potionEffect.getType()));

        bukkitPlayer.setHealth(bukkitPlayer.getMaxHealth());
        bukkitPlayer.setFoodLevel(20);

        // todo: add an option allowing players to choose their own material type
        bukkitPlayer.getInventory().setItem(0, new ItemStack(Material.WOOL, 64));
    }
}
