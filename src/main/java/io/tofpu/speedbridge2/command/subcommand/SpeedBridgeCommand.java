package io.tofpu.speedbridge2.command.subcommand;


import cloud.commandframework.annotations.*;
import com.sk89q.minecraft.util.commands.CommandAlias;
import io.tofpu.speedbridge2.command.parser.IslandArgument;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.MessageUtil;
import io.tofpu.speedbridge2.domain.island.IslandService;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.player.misc.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.CommonBridgePlayer;
import io.tofpu.speedbridge2.plugin.SpeedBridgePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.tofpu.speedbridge2.domain.common.Message.*;
import static io.tofpu.speedbridge2.domain.common.util.MessageUtil.Symbols.ARROW_RIGHT;
import static io.tofpu.speedbridge2.domain.common.util.MessageUtil.Symbols.CROSS;

public final class SpeedBridgeCommand {
    private final IslandService islandService = IslandService.INSTANCE;

    @ProxiedBy("createIsland")
    @CommandMethod("speedbridge create <slot>")
    @CommandDescription("Create an island with a defined slot")
    @CommandPermission("speedbridge.island.create")
    public void onIslandCreate(final CommonBridgePlayer<?> player, final @Argument("slot")
            int slot, @Flag("c") String category, final @Flag("s") String schematic) {
        final CommandSender sender = player.getPlayer();

        if (category == null || category.isEmpty()) {
            category = ConfigurationManager.INSTANCE.getGeneralCategory()
                    .getDefaultIslandCategory();
        }

        if (islandService.createIsland(slot, category) == null) {
            BridgeUtil.sendMessage(sender, String.format(ISLAND_ALREADY_EXISTS,
                    slot + ""));
            return;
        }

        if (schematic == null || schematic.isEmpty()) {
            BridgeUtil.sendMessage(sender, String.format(ISLAND_HAS_BEEN_CREATED,
                    slot + ""));
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
    @CommandDescription("delete an island")
    @CommandPermission("speedbridge.island.delete")
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
    @CommandMethod("speedbridge select <slot>")
    @CommandDescription("select an island to modify their properties")
    @CommandPermission("island.island.select")
    public void onIslandSelect(final CommonBridgePlayer<?> bridgePlayer, final @Argument("slot")
            int slot, final @Flag(value = "c", description = "category") String category,
            final @Flag(value = "s", description = "schematic")
            String schematic) {
        final CommandSender sender = bridgePlayer.getPlayer();
        final Island island = islandService.findIslandBy(slot);

        String message = EMPTY_SELECT;
        boolean successful = false;
        if (island == null) {
            message = String.format(INVALID_ISLAND, slot + "");
        } else {
            String selectType = "";
            if (category != null && !category.isEmpty()) {
                selectType = "category";

                island.setCategory(category);
            } else if (schematic != null && !schematic.isEmpty()) {
                selectType = "schematic";

                successful = island.selectSchematic(schematic);
            }

            switch (selectType) {
                case "category":
                    message = String.format(VALID_SELECT,
                            slot + "", category, selectType);
                    break;
                case "schematic":
                    if (successful) {
                        message = String.format(VALID_SELECT,
                                slot + "", schematic, selectType);
                        break;
                    }
                    message = String.format(INVALID_SCHEMATIC, schematic);
                    break;
            }
        }

        if (!message.isEmpty()) {
            BridgeUtil.sendMessage(sender, message);
        }
    }

    @ProxiedBy("join")
    @CommandMethod("speedbridge join [island]")
    @CommandDescription("Join an island")
    public void onIslandJoin(final BridgePlayer bridgePlayer, final @Argument("island")
            String category) {
        final Player player = Bukkit.getPlayer(bridgePlayer.getPlayerUid());

        // /join 2
        // /join default

        // /randomjoin

        int slot;
        try {
            slot = Integer.parseInt(category);
        } catch (NumberFormatException exception) {
            slot = -1;
        }

        Island island = null;
        if (slot != -1) {
            island = islandService.findIslandBy(slot);
        } else if (category != null && !category.isEmpty()) {
            island = islandService.findIslandBy(category);
        }

        if (island != null) {
            slot = island.getSlot();
        }

        final String message;
        if (island == null || !island.isReady()) {
            if (slot == -1) {
                message = INVALID_ISLAND_ARGUMENT;
            } else {
                message = String.format(INVALID_ISLAND, slot + "");
            }
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

    @ProxiedBy("randomjoin")
    @CommandMethod("speedbridge randomjoin")
    @CommandDescription("Chooses a random island for you")
    public void onRandomJoin(final BridgePlayer bridgePlayer) {
        final Optional<Island> optionalIsland =
                islandService.getAllIslands().stream().parallel()
                        .filter(Island::isReady).findAny();

        final String message;

        if (bridgePlayer.isPlaying()) {
            message = ALREADY_IN_A_ISLAND;
        } else if (!optionalIsland.isPresent()) {
            message = NO_AVAILABLE_ISLAND;
        } else {
            final Island island = optionalIsland.get();
            island.generateGame(bridgePlayer);

            message = String.format(JOINED_AN_ISLAND, island.getSlot() + "");
        }

        BridgeUtil.sendMessage(bridgePlayer, message);
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

    @CommandMethod("speedbridge reload")
    @CommandDescription("Reloads the config")
    @CommandPermission("speedbridge.reload")
    public void pluginReload(final CommonBridgePlayer<?> player) {
        Message.load(SpeedBridgePlugin.getPlugin(SpeedBridgePlugin.class).getDataFolder());
        ConfigurationManager.INSTANCE.reload().whenComplete((unused, throwable) -> {
           if (player.getPlayer() != null) {
               BridgeUtil.sendMessage(player, RELOADED);
           }
        });
    }

    @CommandMethod("speedbridge")
    @CommandDescription("Shows a list of commands")
    @CommandPermission("speedbridge.help")
    @Hidden
    public void onNoArgument(final CommonBridgePlayer<?> bridgePlayer) {
        final CommandSender player = bridgePlayer.getPlayer();
        HelpCommandGenerator.showHelpMessage(player);
    }

    @CommandMethod("speedbridge help")
    @CommandPermission("speedbridge.help")
    @CommandDescription("Shows a list of commands")
    public void onHelpCommand(final CommonBridgePlayer<?> bridgePlayer) {
        final CommandSender player = bridgePlayer.getPlayer();
        HelpCommandGenerator.showHelpMessage(player);
    }
}
