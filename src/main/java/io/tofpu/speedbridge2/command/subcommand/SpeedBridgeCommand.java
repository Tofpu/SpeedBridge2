package io.tofpu.speedbridge2.command.subcommand;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.ProxiedBy;
import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.EmptyBridgePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.service.IslandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpeedBridgeCommand {
    private static final String ISLAND_ALREADY_EXISTS = "%s island is already defined";
    private static final String ISLAND_HAS_BEEN_CREATED = "%s island has been created";
    private static final String ISLAND_HAS_BEEN_CREATED_SCHEMATIC =
            ISLAND_HAS_BEEN_CREATED + " with %s chosen as a schematic";

    private static final String SELECTED_SCHEMATIC = "%s island has selected %s as a " +
            "schematic";

    private static final String INVALID_SCHEMATIC = "%s schematic cannot be found";
    private static final String INVALID_ISLAND = "%s is not an island";

    private static final String ALREADY_IN_A_ISLAND = "You're already in an island";
    private static final String NOT_IN_A_ISLAND = "You're not in an island";

    private final IslandService islandService = IslandService.INSTANCE;

    @ProxiedBy("createIsland")
    @CommandMethod("speedbridge create <slot> [schematic]")
    @CommandDescription("Create an island with a defined slot")
    public void onIslandCreate(final EmptyBridgePlayer player, final @Argument("slot") int slot, final @Argument("schematic") String schematic) {
        final CommandSender sender = player.getSender();

        if (islandService.createIsland(slot) == null) {
            sender.sendMessage(String.format(ISLAND_ALREADY_EXISTS, slot + ""));
            return;
        }

        if (schematic.isEmpty()) {
            sender.sendMessage(String.format(ISLAND_HAS_BEEN_CREATED, slot + ""));
            return;
        }

        final Island island = islandService.findIslandBy(slot);
        if (island.selectSchematic(schematic)) {
            sender.sendMessage(String.format(SELECTED_SCHEMATIC, slot + "", schematic));
        } else {
            sender.sendMessage(String.format(INVALID_SCHEMATIC, schematic));
        }
    }

    @ProxiedBy("selectIsland")
    @CommandMethod("speedbridge select <slot> <schematic>")
    @CommandDescription("Select a schematic for a particular slot")
    public void onIslandSelect(final EmptyBridgePlayer bridgePlayer,
            final @Argument("slot") int slot, final @Argument("schematic") String schematic) {
        final CommandSender sender = bridgePlayer.getSender();
        final Island island = islandService.findIslandBy(slot);
        if (island.selectSchematic(schematic)) {
            sender.sendMessage(String.format(ISLAND_HAS_BEEN_CREATED_SCHEMATIC, slot + "", schematic));
        } else {
            sender.sendMessage(String.format(INVALID_SCHEMATIC, schematic));
        }
    }

    @ProxiedBy("join")
    @CommandMethod("speedbridge join <slot>")
    @CommandDescription("Join an island")
    public void onIslandJoin(final BridgePlayer bridgePlayer,
            final @Argument("slot") int slot) {
        final Player player = Bukkit.getPlayer(bridgePlayer.getPlayerUid());

        final Island island = islandService.findIslandBy(slot);
        if (island == null) {
            player.sendMessage(String.format(INVALID_ISLAND, slot + ""));
            return;
        } else if (bridgePlayer.isPlaying()) {
            player.sendMessage(ALREADY_IN_A_ISLAND);
            return;
        }

        island.generateGame(bridgePlayer);
    }

    @ProxiedBy("leave")
    @CommandMethod("speedbridge leave")
    @CommandDescription("Leave an island")
    public void onIslandLeave(final BridgePlayer bridgePlayer) {
        final Player player = bridgePlayer.getPlayer();

        if (!bridgePlayer.isPlaying()) {
            player.sendMessage(NOT_IN_A_ISLAND);
            return;
        }

        bridgePlayer.getGamePlayer().getCurrentGame().getIsland().leaveGame(bridgePlayer);
    }
}
