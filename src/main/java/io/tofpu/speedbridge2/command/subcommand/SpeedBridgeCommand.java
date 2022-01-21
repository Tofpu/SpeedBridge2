package io.tofpu.speedbridge2.command.subcommand;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.ProxiedBy;
import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.EmptyBridgePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.service.IslandService;
import io.tofpu.speedbridge2.util.BridgeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpeedBridgeCommand {
    private static final String ISLAND_ALREADY_EXISTS = "<red>%s island has already " + "been defined";
    private static final String ISLAND_HAS_BEEN_CREATED = "<gold>%s island has been created";
    private static final String ISLAND_HAS_BEEN_CREATED_SCHEMATIC = ISLAND_HAS_BEEN_CREATED + " with %s chosen as a schematic";

    private static final String SELECTED_SCHEMATIC = "<gold>%s island has selected %s " + "as a " + "schematic";

    private static final String INVALID_SCHEMATIC = "<red>%s schematic cannot be found";
    private static final String INVALID_ISLAND = "<red>%s is not an island";

    private static final String ALREADY_IN_A_ISLAND = "<red>You're already in an island";
    private static final String NOT_IN_A_ISLAND = "<red>You're not in an island";

    private final IslandService islandService = IslandService.INSTANCE;

    @ProxiedBy("createIsland")
    @CommandMethod("speedbridge create <slot> [schematic]")
    @CommandDescription("Create an island with a defined slot")
    public void onIslandCreate(final EmptyBridgePlayer player, final @Argument("slot") int slot, final @Argument("schematic") String schematic) {
        final CommandSender sender = player.getSender();

        if (islandService.createIsland(slot) == null) {
            BridgeUtil.sendMessage(sender, String.format(ISLAND_ALREADY_EXISTS, slot + ""));
            return;
        }

        if (schematic.isEmpty()) {
            BridgeUtil.sendMessage(sender, String.format(ISLAND_HAS_BEEN_CREATED, slot + ""));
            return;
        }

        final Island island = islandService.findIslandBy(slot);
        final String message;
        if (island.selectSchematic(schematic)) {
            message = String.format(SELECTED_SCHEMATIC, slot + "", schematic);
            ;
        } else {
            message = String.format(INVALID_SCHEMATIC, schematic);
            ;
        }
        BridgeUtil.sendMessage(sender, message);
    }

    @ProxiedBy("selectIsland")
    @CommandMethod("speedbridge select <slot> <schematic>")
    @CommandDescription("Select a schematic for a particular slot")
    public void onIslandSelect(final EmptyBridgePlayer bridgePlayer,
            final @Argument("slot") int slot, final @Argument("schematic") String schematic) {
        final CommandSender sender = bridgePlayer.getSender();
        final Island island = islandService.findIslandBy(slot);
        final String message;
        if (island.selectSchematic(schematic)) {
            message = String.format(ISLAND_HAS_BEEN_CREATED_SCHEMATIC, slot + "", schematic);
        } else {
            message = String.format(INVALID_SCHEMATIC, schematic);
        }
        BridgeUtil.sendMessage(sender, message);
    }

    @ProxiedBy("join")
    @CommandMethod("speedbridge join <slot>")
    @CommandDescription("Join an island")
    public void onIslandJoin(final BridgePlayer bridgePlayer,
            final @Argument("slot") int slot) {
        final Player player = Bukkit.getPlayer(bridgePlayer.getPlayerUid());

        final Island island = islandService.findIslandBy(slot);
        final String message;
        if (island == null) {
            message = String.format(INVALID_ISLAND, slot + "");
        } else if (bridgePlayer.isPlaying()) {
            message = ALREADY_IN_A_ISLAND;
        } else {
            message = "";
            island.generateGame(bridgePlayer);
        }

        if (!message.isEmpty()) {
            BridgeUtil.sendMessage(player, message);
        }
    }

    @ProxiedBy("leave")
    @CommandMethod("speedbridge leave")
    @CommandDescription("Leave an island")
    public void onIslandLeave(final BridgePlayer bridgePlayer) {
        final Player player = bridgePlayer.getPlayer();
        final String message;
        if (!bridgePlayer.isPlaying()) {
            message = NOT_IN_A_ISLAND;
        } else {
            message = "";
            bridgePlayer.getGamePlayer().getCurrentGame().getIsland().leaveGame(bridgePlayer);
        }

        if (!message.isEmpty()) {
            BridgeUtil.sendMessage(player, message);
        }
    }
}
