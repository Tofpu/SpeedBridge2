package io.tofpu.speedbridge2.setup.service;

import io.github.revxrsal.eventbus.EventBus;
import io.tofpu.speedbridge2.Constants;
import io.tofpu.speedbridge2.arena.Arena;
import io.tofpu.speedbridge2.arena.ArenaManager;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.service.IslandService;
import io.tofpu.speedbridge2.lobby.LobbyTeleporter;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.tofpu.speedbridge2.setup.domain.SetupInfo;
import io.tofpu.speedbridge2.setup.domain.event.SetupStartEvent;
import io.tofpu.speedbridge2.setup.domain.event.SetupStopEvent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetupService {
    private final EventBus eventBus;
    private final IslandService islandService;
    private final LobbyTeleporter lobbyTeleporter;
    private final ArenaManager<Integer> arenaManager;

    private final Map<UUID, IslandSetup> playerSetups = new HashMap<>();

    public SetupService(EventBus eventBus, IslandService islandService, LobbyTeleporter lobbyTeleporter, World world) {
        this.eventBus = eventBus;
        this.islandService = islandService;
        this.lobbyTeleporter = lobbyTeleporter;
        this.arenaManager = new ArenaManager<>(world, Constants.ArenaPositioning.SETUP);
    }

    public boolean createSetup(Player player, SetupInfo setupInfo) {
        if (playerSetups.containsKey(player.getUniqueId())) {
            return false;
        }

        // todo: handle the exceptions
        Schematic schematic = setupInfo.schematic();

        UUID playerId = player.getUniqueId();
        Arena arena = arenaManager.generateArena(setupInfo.slot(), schematic);
        if (arena == null) {
            throw new IllegalStateException("Could not create arena!");
        }

        IslandSetup setup = new IslandSetup(player, setupInfo.slot(), schematic, arena);
        this.playerSetups.put(playerId, setup);

        arena.teleport(player);
        eventBus.post(SetupStartEvent.class, setup);

        return true;
    }

    public boolean cancelSetup(@NotNull Player player) {
        IslandSetup setup = playerSetups.get(player.getUniqueId());
        if (setup == null) {
            return false;
        }

        eventBus.post(SetupStopEvent.class, setup, SetupStopEvent.Type.CANCELLED);
        teleportPlayerToLobby(setup.player());

        cleanUp(setup.slot(), player);
        return true;
    }

    public void finishSetup(IslandSetup setup) {
        if (!setup.canBeFinished()) {
            // todo: throw exception here? as it's unexpected
            setup.player().sendMessage(ChatColor.RED + "You need to set a spawn point before finishing the setup!");
            setup.player().sendMessage(ChatColor.RED + "Or you can cancel the setup by using the setup tools.");
            return;
        }

        eventBus.post(SetupStopEvent.class, setup, SetupStopEvent.Type.SUCCESS);

        Island newIsland = new Island(setup.slot(), setup.schematic(), setup.spawnPoint());
        islandService.registerIsland(newIsland);

        teleportPlayerToLobby(setup.player());
        setup.player().sendMessage(ChatColor.GREEN + "Setup for slot " + setup.slot() + " finished successfully!");

        cleanUp(setup.slot(), setup.player());
    }

    public void cleanUp(int slot, Player player) {
        playerSetups.remove(player.getUniqueId());
        arenaManager.destroyArena(slot, () -> {});
    }

    private void teleportPlayerToLobby(Player player) {
        lobbyTeleporter.teleportToLobby(player);
    }

    public Optional<IslandSetup> islandSetup(Player player) {
        return Optional.ofNullable(playerSetups.get(player.getUniqueId()));
    }

    public void finishSetup(Player player) {
        islandSetup(player).ifPresent(this::finishSetup);
    }
}
