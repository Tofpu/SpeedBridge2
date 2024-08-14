package io.tofpu.speedbridge2.command.subcommand;

import io.tofpu.speedbridge2.command.NameAndUUID;
import io.tofpu.speedbridge2.command.condition.annotation.*;
import io.tofpu.speedbridge2.command.help.HelpMessageProvider;
import io.tofpu.speedbridge2.command.parser.annotation.PlayerUUID;
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
import io.tofpu.speedbridge2.model.player.ResetType;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.CommonBridgePlayer;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.plugin.SpeedBridgePlugin;
import io.tofpu.speedbridge2.util.material.MaterialCategory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.CommandHandlerVisitor;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.tofpu.speedbridge2.model.common.Message.INSTANCE;
import static io.tofpu.speedbridge2.model.common.util.MessageUtil.Symbols.*;

@Command({"sb", "speedbridge"})
public final class SpeedBridgeCommand implements CommandHandlerVisitor {
    private static final String EMPTY_SCORE = "<red>You haven't scored anything yet";
    private static final String FORMATTED_SCORE =
            " <gold><bold>" + CROSS.getSymbol() + " " + "<reset><yellow>Island " +
                    "<gold>%s</gold>" + " " + ARROW_RIGHT.getSymbol() +
                    " <gold>%s</gold> seconds";

    private final HelpMessageProvider helpMessageProvider;
    private final PlayerService playerService;
    private final IslandService islandService;

    public SpeedBridgeCommand(final HelpMessageProvider helpMessageProvider, final PlayerService playerService, final IslandService islandService) {
        this.helpMessageProvider = helpMessageProvider;
        this.playerService = playerService;
        this.islandService = islandService;
    }

    @Override
    public void visit(@NotNull CommandHandler handler) {
        handler.register(this);
        handler.register(new PlayerSubCommand());
        handler.register(new SetupCommand());
    }

    @DefaultFor("~")
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
                .thenRun(() -> BridgeUtil.sendMessage(bridgePlayer, INSTANCE.lobbySetLocation));
    }

    @Subcommand("create")
    @Usage("<slot> <schematic> [-c category]")
    @Description("Create an island")
    @CommandPermission("speedbridge.island.create")
    @RestrictSetup
    @RestrictDummyModel
    @RestrictConsole
    public String onIslandCreate(final BridgePlayer player, final int slot, final String schematic,
                                 @Optional @Flag("c") String category) {
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

    @Subcommand("modify")
    @Usage("<slot> [-c category|-s schematic]")
    @Description("Modify an island properties")
    @CommandPermission("speedbridge.island.modify")
    public String onIslandSelect(final Island island,
                                 final @Optional @Flag(value = "c") @Default("") String category,
                                 final @Optional @Flag(value = "s") @Default("") String schematic) {
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
//    @Subcommand("join")
    @Usage("<island>")
    @Description("Join an island")
    @RestrictDummyModel
    @RestrictConsole
    public String onIslandJoin(final BridgePlayer sender, final Island island, @OptionalPermission("sb.join.other") BridgePlayer target) {
        if (!isGeneralSetupComplete(sender)) {
            return "";
        }

        if (target == null && sender.isPlaying()) {
            return INSTANCE.alreadyInAIsland;
        } else if (target != null && target.isPlaying()) {
            return String.format(INSTANCE.otherIsAlreadyInAIsland, target.getName());
        }

        if (!island.isReady()) {
            return String.format(INSTANCE.invalidIsland, island.getSlot());
        }

        if (target == null) {
            island.join(sender);
            return String.format(INSTANCE.joinedAnIsland, island.getSlot());
        }

        island.join(target);
        return String.format(INSTANCE.otherJoinedAnIsland, target.getName(), island.getSlot());
    }

    @Command({"sb leave", "speedbridge leave", "leave"})
    @Description("Leave an island")
    public String onIslandLeave(final BridgePlayer sender,
                                final @Optional GameIsland senderGame,
                                @OptionalPermission("sb.leave.other") BridgePlayer target) {
        if (Objects.equals(sender, target)) {
            target = null;
        }

        System.out.println("onIslandLeave command called");
        if (senderGame == null && target == null) {
            return String.format(INSTANCE.notInAIsland);
        } else if (target == null) {
            senderGame.stopGame();
            return ""; // handled by method
        }

        GameIsland targetGame = target.getCurrentGame();
        int slot = targetGame == null ? -1 : targetGame.getIsland().getSlot();
        if (targetGame == null || !targetGame.stopGame()) {
            return String.format(INSTANCE.otherNotInAIsland, target.getName(), slot);
        }
        return String.format(INSTANCE.otherLeftTheIsland, target.getName(), slot);
    }

    @Command({"sb score", "speedbridge score" ,"score"})
    @Description("Shows a list of your scores")
    @RestrictConsole
    public String onScore(final BridgePlayer sender,
                          @OptionalPermission("sb.score.other") BridgePlayer target) {
        boolean isSender = target == null;
        if (isSender) {
            target = sender;
        }

        final List<String> scoreList = new ArrayList<>();

        for (final Score score : target.getScores()) {
            if (scoreList.isEmpty()) {
                scoreList.add(isSender ? INSTANCE.scoreTitle : String.format(INSTANCE.otherScoreTitle, target.getName()));
            }

            scoreList.add(String.format(FORMATTED_SCORE, score.getScoredOn(), BridgeUtil.formatNumber(score.getScore())));
        }

        if (scoreList.isEmpty()) {
            return EMPTY_SCORE;
        }

        scoreList.add("");
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
    @CommandPermission("sb.islands")
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
    public void onHelp(final CommonBridgePlayer<?> bridgePlayer, final ExecutableCommand command) {
        CommandCategory parent = command.getParent();
        if (parent == null) {
            return;
        }
        helpMessageProvider.showHelpMessage(parent.getName(), bridgePlayer.getPlayer());
    }

    @Command({"sb randomjoin", "speedbridge randomjoin" ,"randomjoin"})
    @Description("Chooses a random island for you")
    @RestrictSetup
    @RestrictDummyModel
    @RestrictConsole
    @AutoComplete("@players")
    @CommandPermission("sb.randomjoin")
    public String onRandomJoin(
            final BridgePlayer sender,
            @OptionalPermission("sb.randomjoin.other") final BridgePlayer target) {
        if (!isGeneralSetupComplete(sender)) {
            return "";
        }

        if (sender.isPlaying()) {
            return INSTANCE.alreadyInAIsland;
        }

        final java.util.Optional<Island> optionalIsland = getRandomIsland();

        if (!optionalIsland.isPresent()) {
            return INSTANCE.noAvailableIsland;
        }

        final Island island = optionalIsland.get();
        if (target == null || target.equals(sender)) {
            island.join(sender);
            return String.format(INSTANCE.joinedAnIsland, island.getSlot());
        }

        island.join(target);
        return String.format(INSTANCE.otherJoinedAnIsland, target.getName(), island.getSlot());
    }

    @NotNull
    private java.util.Optional<Island> getRandomIsland() {
        final List<Island> filteredIslands = islandService.getAllIslands()
                .stream()
                .parallel()
                .filter(Island::isReady)
                .collect(Collectors.toList());

        if (filteredIslands.isEmpty()) {
            return java.util.Optional.empty();
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(filteredIslands.size());
        return java.util.Optional.ofNullable(filteredIslands.get(randomIndex));
    }

    @Subcommand("setup")
    class SetupCommand {
        @Subcommand("create")
        @Description("Create an island setup")
        @CommandPermission("speedbridge.setup.admin")
        @RestrictDummyModel
        @RestrictSetup
        @RestrictConsole
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

        @Subcommand("setspawn")
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

        @Subcommand("finish")
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

        @Subcommand("cancel")
        @Description("Cancels the island's setup")
        @CommandPermission("speedbridge.setup.admin")
        @RestrictSetup(opposite = true)
        @RestrictConsole
        public String cancelSetup(final BridgePlayer bridgePlayer) {
            final IslandSetup islandSetup = IslandSetupHandler.INSTANCE.findSetupBy(bridgePlayer.getPlayerUid());

            islandSetup.cancel();
            return INSTANCE.setupCancelled;
        }
    }

    private String hover(final String hoverContent, final String content) {
        return "<hover:show_text:'" + hoverContent + "'>" + content;
    }

    private <T> void onCompletion(final CompletableFuture<T> future, final Consumer<T> consumer) {
        future.whenComplete((t, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            consumer.accept(t);
        });
    }

    @Subcommand("player")
    class PlayerSubCommand {
        @Subcommand("set block")
        @Description("Changes the selected block type for a specified player")
        @CommandPermission("speedbridge.player.set.block")
        @AutoComplete("* @players")
        @Usage("<material> <target>")
        public String setSelectedBlockType(final @MaterialType(category = MaterialCategory.BLOCK) Material material, final BridgePlayer target) {
            if (!material.isSolid()) {
                return String.format(INSTANCE.blockTypeMustBeSolid, material);
            }

            if (target.getChoseMaterial() == material) {
                return String.format(INSTANCE.blockAlreadySelected, target.getName(), material);
            }

            target.setChosenMaterial(material);
            return String.format(INSTANCE.setChosenType, target.getName(), material);
        }

        @Subcommand("reset")
        @Usage("<target> <all|scores|stats>")
        @Description("Resets player properties")
        @CommandPermission("speedbridge.player.reset")
        @AutoComplete("@players *")
        public void onPlayerReset(final CommonBridgePlayer<?> sender, final @PlayerUUID NameAndUUID target,
                                  final ResetType type) {
            String targetName = target.playerName();
            UUID targetId = target.playerUUID();

            onCompletion(playerService.reset(targetId, type), unused -> {
                String message = null;
                switch (type) {
                    case ALL:
                        message = String.format(INSTANCE.playerWiped, targetName);
                        break;
                    case SCORES:
                        message = String.format(INSTANCE.playerScoreReset, targetName);
                        break;
                    case STATS:
                        message = String.format(INSTANCE.playerStatsReset, targetName);
                        break;
                }
                if (message == null) return;
                BridgeUtil.sendMessage(sender, message);
            });
        }
    }
}
