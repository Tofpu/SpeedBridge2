package io.tofpu.speedbridge2.command.subcommand;


import cloud.commandframework.annotations.*;
import com.sk89q.minecraft.util.commands.CommandAlias;
import io.tofpu.speedbridge2.command.parser.IslandArgument;
import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.CommonBridgePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.misc.Score;
import io.tofpu.speedbridge2.domain.service.IslandService;
import io.tofpu.speedbridge2.util.BridgeUtil;
import io.tofpu.speedbridge2.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.tofpu.speedbridge2.util.MessageUtil.Symbols.ARROW_RIGHT;
import static io.tofpu.speedbridge2.util.MessageUtil.Symbols.CROSS;

public final class SpeedBridgeCommand {
    public static final String ERROR =
            "<red>" + MessageUtil.Symbols.WARNING.getSymbol() +
            " ";

    private static final String ISLAND_ALREADY_EXISTS =
            ERROR + "Island %s already exists!";
    private static final String ISLAND_HAS_BEEN_CREATED = "<green>Island %s has been " +
            "created!";
    private static final String ISLAND_HAS_BEEN_CREATED_SCHEMATIC =
            ISLAND_HAS_BEEN_CREATED + " with %s chosen as a schematic!";

    private static final String SELECTED_SCHEMATIC = "<green>Island %s has been selected" +
            " %s " +
            "as a " + "schematic!";

    private static final String INVALID_SCHEMATIC = ERROR + "Schematic %s cannot be " +
            "found!";
    private static final String INVALID_ISLAND = ERROR + "Island %s cannot be found!";

    private static final String ALREADY_IN_A_ISLAND = ERROR + "You're already on an " +
            "island!";

    private static final String SCORE_TITLE_BAR = MessageUtil.CHAT_BAR.substring(0, MessageUtil.CHAT_BAR
            .length() / 6);
    private static final String SCORE_TITLE = "<yellow>" + SCORE_TITLE_BAR + "  " +
            "<gold><bold" + "> YOUR SCORES</bold></gold>" + " " + SCORE_TITLE_BAR;

    private static final String JOINED_AN_ISLAND = "<yellow>You joined the island %s!";

    private final IslandService islandService = IslandService.INSTANCE;

    @ProxiedBy("createIsland")
    @CommandMethod("speedbridge create <slot> [schematic]")
    @CommandDescription("Create an island with a defined slot")
    public void onIslandCreate(final CommonBridgePlayer<?> player, final @Argument(
            "slot") int slot, final @Argument(value = "schematic") String schematic) {
        final CommandSender sender = player.getPlayer();

        if (islandService.createIsland(slot) == null) {
            BridgeUtil.sendMessage(sender, String.format(ISLAND_ALREADY_EXISTS, slot + ""));
            return;
        }

        if (schematic == null || schematic.isEmpty()) {
            BridgeUtil.sendMessage(sender, String.format(ISLAND_HAS_BEEN_CREATED, slot + ""));
            return;
        }

        final Island island = islandService.findIslandBy(slot);
        final String message;
        if (island.selectSchematic(schematic)) {
            message = String.format(ISLAND_HAS_BEEN_CREATED_SCHEMATIC, slot + "", schematic);
        } else {
            message = String.format(INVALID_SCHEMATIC, schematic);
        }
        BridgeUtil.sendMessage(sender, message);
    }

    @ProxiedBy("deleteIsland")
    @CommandMethod("speedbridge delete <slot>")
    @CommandDescription("Remove an island")
    public void onIslandDelete(final CommonBridgePlayer<?> player, final @Argument(
            "slot") int slot) {
        final Island island = islandService.deleteIsland(slot);

        final String message;
        if (island == null) {
            message = String.format(INVALID_ISLAND, slot + "");
        } else {
            message = String.format(DELETED_AN_ISLAND, slot);
        }
        BridgeUtil.sendMessage(player, message);
    }

    @ProxiedBy("selectIsland")
    @CommandMethod("speedbridge select <slot> <schematic>")
    @CommandDescription("Select a schematic for a particular slot")
    public void onIslandSelect(final CommonBridgePlayer<?> bridgePlayer,
            final @Argument("slot") int slot, final @Argument("schematic") String schematic) {
        final CommandSender sender = bridgePlayer.getPlayer();
        final Island island = islandService.findIslandBy(slot);
        final String message;

        if (island == null) {
            message = String.format(INVALID_ISLAND, slot + "");
        } else if (island.selectSchematic(schematic)) {
            message = String.format(SELECTED_SCHEMATIC, slot + "", schematic);
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
            message = String.format(JOINED_AN_ISLAND, slot + "");
            island.generateGame(bridgePlayer);
        }

        if (!message.isEmpty()) {
            BridgeUtil.sendMessage(player, message);
        }
    }

    @ProxiedBy("leave")
    @CommandMethod("speedbridge leave")
    @CommandDescription("Leave an island")
    @IslandArgument
    public void onIslandLeave(final BridgePlayer bridgePlayer, final @NotNull Island island) {
        island.leaveGame(bridgePlayer);
    }

    @ProxiedBy("score")
    @CommandMethod("speedbridge score")
    @CommandAlias("speedbridge score")
    @CommandDescription("Shows a list of your scores")
    public void onScore(final BridgePlayer bridgePlayer) {
        final Player player = bridgePlayer.getPlayer();
        final List<String> scoreList = new ArrayList<>();
        final String message;

        for (final Score score : bridgePlayer.getScores()) {
            if (scoreList.isEmpty()) {
                scoreList.add(SCORE_TITLE);
            }
            // Your scores:
            // Island X scored X seconds;
            final String formattedScore = " <gold><bold>" + CROSS.getSymbol() + " " + "<reset><yellow>Island " + "<gold>%s</gold>" + " " + ARROW_RIGHT
                    .getSymbol() + " <gold>%s</gold> seconds";
            scoreList.add(String.format(formattedScore, score.getScoredOn(), BridgeUtil.toFormattedScore(score
                    .getScore())));
        }

        if (scoreList.isEmpty()) {
            message = "<red>You haven't scored anything yet";
        } else {
            scoreList.add(MessageUtil.MENU_BAR);

            message = String.join("\n", scoreList);
        }

        BridgeUtil.sendMessage(player, message);
    }

    @CommandMethod("speedbridge")
    @Hidden
    @CommandDescription("Shows a list of commands")
    public void onNoArgument(final CommonBridgePlayer<?> bridgePlayer) {
        final CommandSender player = bridgePlayer.getPlayer();
        HelpCommandGenerator.showHelpMessage(player);
    }

    @CommandMethod("speedbridge help")
    @CommandDescription("Shows a list of commands")
    public void onHelpCommand(final CommonBridgePlayer<?> bridgePlayer) {
        final CommandSender player = bridgePlayer.getPlayer();
        HelpCommandGenerator.showHelpMessage(player);
    }
}
