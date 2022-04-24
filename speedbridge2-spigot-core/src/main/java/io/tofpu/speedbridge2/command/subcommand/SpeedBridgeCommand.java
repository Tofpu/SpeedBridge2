package io.tofpu.speedbridge2.command.subcommand;

import io.tofpu.speedbridge2.command.condition.annotation.RestrictConsole;
import io.tofpu.speedbridge2.command.condition.annotation.RestrictDummyModel;
import io.tofpu.speedbridge2.command.condition.annotation.RestrictSetup;
import io.tofpu.speedbridge2.model.blockmenu.BlockMenuManager;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterHolderImpl;
import io.tofpu.speedbridge2.model.common.presenter.type.MessagePairPresenter;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.IslandHandler;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.setup.IslandSetup;
import io.tofpu.speedbridge2.model.island.object.setup.IslandSetupHandler;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.CommonBridgePlayer;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.plugin.SpeedBridgePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.model.common.Message.INSTANCE;
import static io.tofpu.speedbridge2.model.common.util.MessageUtil.Symbols.ARROW_RIGHT;
import static io.tofpu.speedbridge2.model.common.util.MessageUtil.Symbols.CHECK_MARK;
import static io.tofpu.speedbridge2.model.common.util.MessageUtil.Symbols.CROSS;

@Command({"sb", "speedbridge"})
public final class SpeedBridgeCommand {
    private static final String EMPTY_SCORE = "<red>You haven't scored anything yet";
    private static final String FORMATTED_SCORE =
            " <gold><bold>" + CROSS.getSymbol() + " " + "<reset><yellow>Island " +
            "<gold>%s</gold>" + " " + ARROW_RIGHT.getSymbol() +
            " <gold>%s</gold> seconds";

    private final PlayerService playerService;
    private final IslandService islandService;

    public SpeedBridgeCommand(final PlayerService playerService, final IslandService islandService) {
        this.playerService = playerService;
        this.islandService = islandService;
    }

    @Default
    @Description("The Main Command")
    public String defaultCommand() {
        return INSTANCE.noArgument;
    }

    @Subcommand("setlobby")
    @Description("Sets the lobby location")
    @CommandPermission("speedbridge.lobby.set")
    @RestrictSetup
    @RestrictConsole
    public void onLobbySet(final BridgePlayer bridgePlayer) {
        ConfigurationManager.INSTANCE.getLobbyCategory()
                .setLobbyLocation(bridgePlayer.getPlayer()
                        .getLocation())
                .whenComplete((unused, throwable) -> {
                    BridgeUtil.sendMessage(bridgePlayer, INSTANCE.lobbySetLocation);
                });
    }

    @Subcommand("create")
    @Usage("create <slot> <schematic> [-c category]")
    @Description("Create an island")
    @CommandPermission("speedbridge.island.create")
    @RestrictSetup
    @RestrictDummyModel
    @RestrictConsole
    public String onIslandCreate(final BridgePlayer player, final int slot, final String schematic,
            @revxrsal.commands.annotation.Optional @Flag("c") String category) {
        if (!isGeneralSetupComplete(player)) {
            return "";
        }

        if (category == null || category.isEmpty()) {
            category = ConfigurationManager.INSTANCE.getGeneralCategory()
                    .getDefaultIslandCategory();
        }

        final IslandHandler.IslandCreationResult result = islandService.createIsland(slot, category, schematic);
        final IslandHandler.IslandCreationResultType resultType = result.getResult();

        BridgeUtil.debug("SpeedBridgeCommand#onIslandCreate(): resultType: " + resultType.name());

        if (resultType == IslandHandler.IslandCreationResultType.ISLAND_ALREADY_EXISTS) {
            return String.format(INSTANCE.islandAlreadyExists, slot + "");
        }

        if (resultType == IslandHandler.IslandCreationResultType.UNKNOWN_SCHEMATIC) {
            return String.format(INSTANCE.unknownSchematic, schematic);
        }

        // initiate the creation setup process
        IslandSetupHandler.INSTANCE.initiate(player, result.getIsland());

        // notify the player about the setup
        BridgeUtil.sendMessage(player, INSTANCE.startingSetupProcess);

        return String.format(INSTANCE.islandSetupNotification.replace("%slot%",
                slot + ""));
    }

    @Subcommand("delete")
    @Description("Delete an island")
    @CommandPermission("speedbridge.island.delete")
    public String onIslandDelete(final Island target) {
        target.delete();

        return String.format(INSTANCE.deletedAnIsland, target.getSlot());
    }

    @Subcommand("reset")
    @Description("Wipes the player's data")
    @CommandPermission("speedbridge.player.reset")
    public void onPlayerReset(final CommonBridgePlayer<?> bridgePlayer, final String name) {
        BridgeUtil.runBukkitAsync(() -> {
            final CommandSender sender = bridgePlayer.getPlayer();
            if (sender == null) {
                return;
            }

            final UUID uuidResult = BridgeUtil.findUUIDBy(name);
            if (uuidResult == null) {
                BridgeUtil.sendMessage(bridgePlayer, String.format(INSTANCE.playerDoesntExist, name));
                return;
            }

            BridgePlayer target = playerService.getIfPresent(uuidResult);
            if (target == null) {
                try {
                    playerService.loadAsync(uuidResult)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    BridgeUtil.sendMessage(bridgePlayer, INSTANCE.somethingWentWrong);
                    throw new IllegalStateException(e);
                }
            }

            if (target == null) {
                BridgeUtil.sendMessage(bridgePlayer, String.format(INSTANCE.playerDoesntExist, name));
                return;
            }

            try {
                target.reset()
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                BridgeUtil.sendMessage(bridgePlayer, INSTANCE.somethingWentWrong);
                throw new IllegalStateException(e);
            }

            BridgeUtil.sendMessage(bridgePlayer, String.format(INSTANCE.playerWiped, name));
        });
    }

    @Subcommand("modify")
    @Usage("modify <slot> [-c category|-s schematic]")
    @Description("Modify an island properties")
    @CommandPermission("speedbridge.island.modify")
    public String onIslandSelect(final Island island, final @revxrsal.commands.annotation.Optional
    @Flag(value = "c") @Default("")
            String category, final @revxrsal.commands.annotation.Optional
    @Flag(value = "s") @Default("") String schematic) {
        final int slot = island.getSlot();

        if (!category.isEmpty()) {
            island.setCategory(category);

            return String.format(INSTANCE.validSelect, slot + "", category, "category");
        }

        if (!schematic.isEmpty()) {
            if (island.selectSchematic(schematic)) {
                return String.format(INSTANCE.validSelect,
                        slot + "", schematic, "schematic");
            }

            return String.format(INSTANCE.unknownSchematic, schematic);
        }
        return INSTANCE.emptySelect;
    }

    private boolean isGeneralSetupComplete(final BridgePlayer bridgePlayer) {
        final boolean isLobbyProcessComplete =
                ConfigurationManager.INSTANCE.getLobbyCategory()
                        .getLobbyLocation() != null;

        // if the lobby process is complete, return true
        if (isLobbyProcessComplete) {
            return true;
        }

        final Player player = bridgePlayer.getPlayer();
        // if the player is an operator, or has the "speedbridge.lobby.set" permission
        // node
        if (player.isOp() || player.hasPermission("speedbridge.lobby.set")) {
            BridgeUtil.sendMessage(bridgePlayer, INSTANCE.lobbyMissing);
        } else {
            BridgeUtil.sendMessage(bridgePlayer, INSTANCE.generalSetupIncomplete);
            // forwarding the message to console
            BridgeUtil.sendMessage(Bukkit.getConsoleSender(), INSTANCE.lobbyMissing);
        }

        return false;
    }

    @Command({"sb join", "speedbridge join" ,"join"})
    @Usage("join <island>")
    @Description("Join an island")
    @RestrictDummyModel
    @RestrictConsole
    public String onIslandJoin(final BridgePlayer bridgePlayer, final Island island) {
        if (!isGeneralSetupComplete(bridgePlayer)) {
            return "";
        }

        if (bridgePlayer.isPlaying()) {
            return INSTANCE.alreadyInAIsland;
        }

        if (!island.isReady()) {
            return String.format(INSTANCE.invalidIsland, island.getSlot());
        }

        island.join(bridgePlayer);
        return String.format(INSTANCE.joinedAnIsland, island.getSlot() + "");
    }

    @Command({"sb leave", "speedbridge leave" ,"leave"})
    @Description("Leave an island")
    public void onIslandLeave(final GameIsland gameIsland) {
        gameIsland.stopGame();
    }

    @Command({"sb score", "speedbridge score" ,"score"})
    @Description("Shows a list of your scores")
    @RestrictConsole
    public String onScore(final BridgePlayer bridgePlayer) {
        final List<String> scoreList = new ArrayList<>();

        for (final Score score : bridgePlayer.getScores()) {
            if (scoreList.isEmpty()) {
                scoreList.add(INSTANCE.scoreTitle);
            }

            scoreList.add(String.format(FORMATTED_SCORE, score.getScoredOn(), BridgeUtil.formatNumber(score.getScore())));
        }

        if (scoreList.isEmpty()) {
            return EMPTY_SCORE;
        }

        scoreList.add("\n");
        return String.join("\n", scoreList);
    }

    @Command({"sb choose", "speedbridge choose", "choose"})
    @Description("Lets you choose a block")
    @RestrictDummyModel
    @RestrictConsole
    public void chooseBlock(final BridgePlayer bridgePlayer) {
        BlockMenuManager.INSTANCE.showInventory(bridgePlayer);
    }

    @Command({"sb islands", "speedbridge islands", "islands"})
    public String showIslands() {
        final MessagePresenterHolderImpl holder = new MessagePresenterHolderImpl(
                "<yellow>List of Islands");

        holder.append(() -> {
            final MessagePairPresenter.Builder builder = new MessagePairPresenter.Builder();

            for (final Island island : islandService.getAllIslands()) {
                final String title = "<yellow><bold>Island Analysis<reset>\n";

                final String schematicHover = title + "<yellow>Schematic: " +
                                              (island.getSchematicClipboard() == null ?
                                                      "<red>" + CROSS.getSymbol() :
                                                      "<green>" + CHECK_MARK.getSymbol());

                final String spawnPointHover = "<yellow>Spawnpoint: " +
                                               (island.getAbsoluteLocation() == null ?
                                                       "<red>" + CROSS.getSymbol() :
                                                       "<green>" +
                                                       CHECK_MARK.getSymbol());

                builder.pair("<yellow>Island-" + island.getSlot(), hover(
                        schematicHover + "\n" + spawnPointHover, island.isReady() ?
                                "<green" + ">Ready" : "<red>Not Ready"));
            }
            return builder.build();
        });
        return holder.getResult();
    }

    @Subcommand("reload")
    @Description("Reloads the config")
    @CommandPermission("speedbridge.reload")
    public void pluginReload(final CommonBridgePlayer<?> player) {
        final CompletableFuture<?>[] completableFutures = new CompletableFuture[2];
        completableFutures[0] = Message.load(SpeedBridgePlugin.getPlugin(SpeedBridgePlugin.class)
                .getDataFolder());
        completableFutures[1] = ConfigurationManager.INSTANCE.reload();

        CompletableFuture.allOf(completableFutures)
                .whenComplete((unused, throwable) -> {
                    // reloading the blocks
                    BlockMenuManager.INSTANCE.reload();

                    if (player.getPlayer() != null) {
                        BridgeUtil.sendMessage(player, INSTANCE.reloaded);
                    }
                });
    }

    @Subcommand("help")
    @CommandPermission("speedbridge.help")
    @Description("Shows a list of commands")
    public void onHelpCommand(final CommonBridgePlayer<?> bridgePlayer) {
        final CommandSender player = bridgePlayer.getPlayer();
        HelpCommandGenerator.showHelpMessage(player);
    }

    @Command({"sb randomjoin", "speedbridge randomjoin" ,"randomjoin"})
    @Description("Chooses a random island for you")
    @RestrictSetup
    @RestrictDummyModel
    @RestrictConsole
    public String onRandomJoin(final BridgePlayer bridgePlayer) {
        if (!isGeneralSetupComplete(bridgePlayer)) {
            return "";
        }

        if (bridgePlayer.isPlaying()) {
            return INSTANCE.alreadyInAIsland;
        }

        final Optional<Island> optionalIsland = islandService.getAllIslands()
                .stream()
                .parallel()
                .filter(Island::isReady)
                .findAny();

        if (!optionalIsland.isPresent()) {
            return INSTANCE.noAvailableIsland;
        }

        final Island island = optionalIsland.get();
        island.join(bridgePlayer);

        return String.format(INSTANCE.joinedAnIsland, island.getSlot() + "");
    }

    @Subcommand("setup")
    @Description("Create an island setup")
    @CommandPermission("speedbridge.setup.admin")
    @RestrictDummyModel
    @RestrictSetup
    @RestrictConsole
    @Default
    public String onStartSetup(final BridgePlayer bridgePlayer, final Island island) {
        if (!isGeneralSetupComplete(bridgePlayer)) {
            return "";
        }
        final int slot = island.getSlot();

        if (bridgePlayer.isPlaying()) {
            return INSTANCE.inAGame;
        } else if (island == null) {
            return String.format(INSTANCE.invalidIsland, slot);
        }

        IslandSetupHandler.INSTANCE.initiate(bridgePlayer, island);
        return String.format(INSTANCE.startingSetupProcess, slot);
    }

    @Subcommand("setup setspawn")
    @Description("Sets the island's spawnpoint")
    @CommandPermission("speedbridge.setup.admin")
    @RestrictSetup(opposite = true)
    @RestrictConsole
    public String setupSetSpawn(final BridgePlayer bridgePlayer) {
        final IslandSetup islandSetup = IslandSetupHandler.INSTANCE.findSetupBy(bridgePlayer.getPlayerUid());

        final Location playerLocation = bridgePlayer.getPlayer()
                .getLocation();

        // if the location given was not valid
        if (!islandSetup.isLocationValid(playerLocation)) {
            return INSTANCE.invalidSpawnPoint;
        }

        // otherwise, set the location point
        islandSetup.setPlayerSpawnPoint(playerLocation);

        return INSTANCE.setSpawnPoint + "\n" + INSTANCE.completeNotification;
    }

    @Subcommand("setup finish")
    @Description("Completes the island's setup")
    @CommandPermission("speedbridge.setup.admin")
    @RestrictSetup(opposite = true)
    @RestrictConsole
    public String setupFinish(final BridgePlayer bridgePlayer) {
        final IslandSetup islandSetup = IslandSetupHandler.INSTANCE.findSetupBy(bridgePlayer.getPlayerUid());

        if (!islandSetup.isReady()) {
            return INSTANCE.setupIncomplete;
        }

        islandSetup.finish();
        return INSTANCE.setupComplete;
    }

    @Subcommand("setup cancel")
    @Description("Cancels the island's setup")
    @CommandPermission("speedbridge.setup.admin")
    @RestrictSetup(opposite = true)
    @RestrictConsole
    public String cancelSetup(final BridgePlayer bridgePlayer) {
        final IslandSetup islandSetup = IslandSetupHandler.INSTANCE.findSetupBy(bridgePlayer.getPlayerUid());

        islandSetup.cancel();
        return INSTANCE.setupCancelled;
    }

    private String hover(final String hoverContent, final String content) {
        return "<hover:show_text:'" + hoverContent + "'>" + content;
    }
}
