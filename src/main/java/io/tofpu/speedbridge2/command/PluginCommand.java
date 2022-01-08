package io.tofpu.speedbridge2.command;

import io.tofpu.speedbridge2.database.Databases;
import io.tofpu.speedbridge2.domain.service.IslandService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PluginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final IslandService service = IslandService.INSTANCE;
        switch (args[0]) {
            case "create":
                service.createIsland(Integer.parseInt(args[1]));
                sender.sendMessage("created island on " + args[1] + " slot!");
                break;
            case "change":
                service.findIslandBy(Integer.parseInt(args[1])).setCategory(args[2]);
                sender.sendMessage("updated island");
                break;
            case "delete":
                service.deleteIsland(Integer.parseInt(args[1]));
                sender.sendMessage("deleted island on " + args[1] + " slot");
                break;
            case "islands":
                sender.sendMessage("here's your locally stored islands");
                sender.sendMessage(service.getAllIslands().toString());
                break;
            case "data":
                Databases.ISLAND_DATABASE.getStoredIslands()
                        .whenComplete((islands, throwable) -> {
                            sender.sendMessage("here's your database stored islands");
                            sender.sendMessage(islands.toString());
                        });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + args[0]);
        }

        return false;
    }
}
