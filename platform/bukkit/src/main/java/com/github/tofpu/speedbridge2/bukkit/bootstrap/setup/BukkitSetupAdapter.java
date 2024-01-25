package com.github.tofpu.speedbridge2.bukkit.bootstrap.setup;

import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.SetupToolbar;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.tool.CancelSetupTool;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.tool.SetSpawnTool;
import com.github.tofpu.speedbridge2.bukkit.bootstrap.setup.toolbar.tool.SetupFinishTool;
import com.github.tofpu.speedbridge2.bukkit.plugin.BukkitPlugin;
import com.github.tofpu.speedbridge2.common.PlatformSetupAdapter;
import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupPlayer;
import io.tofpu.toolbar.ToolbarAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitSetupAdapter implements PlatformSetupAdapter {
    private final BukkitPlugin plugin;
    private final ToolbarAPI toolbarAPI;

    private boolean registeredToolBar = false;

    public BukkitSetupAdapter(BukkitPlugin plugin, ToolbarAPI toolbarAPI) {
        this.plugin = plugin;
        this.toolbarAPI = toolbarAPI;
    }

    public void register() {
        if (registeredToolBar) {
            return;
        }
        registerToolbar();
        registeredToolBar = true;
    }

    @Override
    public void onSetupPrepare(IslandSetup islandSetup, IslandSetupPlayer player) {
        if (!registeredToolBar) {
            register();
        }
        Player bukkitPlayer = Bukkit.getPlayer(player.id());
        toolbarAPI.equip("island_setup_toolbar", bukkitPlayer);
    }

    private void registerToolbar() {
        SetupToolbar toolbar = new SetupToolbar("island_setup_toolbar");
        toolbar.addItem(3, new SetupFinishTool(plugin.setupSystem()));
        toolbar.addItem(4, new SetSpawnTool(plugin.setupSystem()));
        toolbar.addItem(5, new CancelSetupTool(plugin.setupSystem()));

        toolbarAPI.registerToolbar(toolbar);
    }

    @Override
    public void onSetupStop(IslandSetup islandSetup, IslandSetupPlayer player) {
        toolbarAPI.unequip(player.id());
    }
}
