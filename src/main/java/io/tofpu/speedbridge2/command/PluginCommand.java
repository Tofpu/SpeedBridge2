package io.tofpu.speedbridge2.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PluginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }
    //
//    @Override
//    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
//        final IslandService islandService = IslandService.INSTANCE;
//        final PlayerService playerService = PlayerService.INSTANCE;
//
//        GamePlayer gamePlayer;
//        Island island;
//        UUID uuid;
//        BridgePlayer bridgePlayer;
//        switch (args[0]) {
//            case "create":
//                if (islandService.createIsland(Integer.parseInt(args[1])) == null) {
//                    sender.sendMessage(args[1] + " island already exists");
//                    return false;
//                }
//                sender.sendMessage("created island on " + args[1] + " slot");
//                break;
//            case "select":
//                island = islandService.findIslandBy(Integer.parseInt(args[1]));
//                final boolean foundSchematic = island.selectSchematic(args[2]);
//
//                if (foundSchematic) {
//                    sender.sendMessage("you have successfully selected a schematic");
//                } else {
//                    sender.sendMessage("the schematic couldn't be found");
//                }
//                break;
//            case "join":
//                gamePlayer = GamePlayer.of((Player) sender);
//
//                if (gamePlayer.isInQueue()) {
//                    gamePlayer.getPlayer().sendMessage("you're already in a queue");
//                    return false;
//                } else if (gamePlayer.isPlaying()) {
//                    gamePlayer.getPlayer().sendMessage("you're already playing");
//                    return false;
//                }
//
//                island = islandService.findIslandBy(Integer.parseInt(args[1]));
//                island.generateGame((Player) sender);
//                break;
//            case "leave":
//                gamePlayer = GamePlayer.of((Player) sender);
//                if (!gamePlayer.isPlaying()) {
//                    sender.sendMessage("you're not playing");
//                    return false;
//                }
//                island = gamePlayer.getCurrentGame().getIsland();
//                island.leaveGame(gamePlayer);
//                break;
//            case "change":
//                island = islandService.findIslandBy(Integer.parseInt(args[1]));
//                if (island == null) {
//                    sender.sendMessage(args[1] + " island cannot be found");
//                    return false;
//                }
//                island.setCategory(args[2]);
//                sender.sendMessage("updated island");
//                break;
//            case "setscore":
//                uuid = ((Player) sender).getUniqueId();
//                bridgePlayer = playerService.get(uuid);
//
//                bridgePlayer.setScoreIfLower(Integer.parseInt(args[1]), Long.parseLong(args[2]));
//                sender.sendMessage("successfully set your score!");
//                break;
//            case "showscore":
//                uuid = ((Player) sender).getUniqueId();
//                bridgePlayer = playerService.get(uuid);
//
//                sender.sendMessage("your score is: " + bridgePlayer.findScoreBy(Integer.parseInt(args[1]))
//                        .getScore());
//                break;
//            case "delete":
//                if (islandService.deleteIsland(Integer.parseInt(args[1])) == null) {
//                    sender.sendMessage(args[1] + " island cannot be found");
//                    return false;
//                }
//                sender.sendMessage("deleted island on " + args[1] + " slot");
//                break;
//            case "islands":
//                sender.sendMessage("here's your locally stored islands");
//                sender.sendMessage(islandService.getAllIslands().toString());
//                break;
//            case "data":
//                Databases.ISLAND_DATABASE.getStoredIslands()
//                        .whenComplete((islands, throwable) -> {
//                            sender.sendMessage("here's your database stored islands");
//                            sender.sendMessage(islands.toString());
//                        })
//                        .thenRun(() -> {
//                            Databases.PLAYER_DATABASE.getStoredPlayers()
//                                    .whenComplete((bridgePlayers, throwable) -> {
//                                        sender.sendMessage("here's your database stored players");
//                                        sender.sendMessage(bridgePlayers.toString());
//                                    });
//                        });
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + args[0]);
//        }
//
//        return false;
//    }
}
