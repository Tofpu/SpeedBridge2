package io.tofpu.speedbridge2.setup.command;

import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.schematic.Schematic;
import io.tofpu.speedbridge2.setup.SetupInfo;
import io.tofpu.speedbridge2.setup.SetupService;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

@Subcommand("setup")
public class SetupCommand extends ChildrenCommand {
    private final SetupService setupService;

    public SetupCommand(SetupService setupService) {
        this.setupService = setupService;
    }

    @Subcommand("create")
    public void createSetup(BukkitCommandActor actor, int slot, Schematic schematic) {
        if (setupService.createSetup(actor.requirePlayer(), new SetupInfo(slot, schematic))) {
            actor.reply(String.format("&eCreated setup for slot %d with schematic %s", slot, schematic.name()));
        } else {
            actor.reply("&cYou already have a setup in progress!");
        }
    }

    @Subcommand("cancel")
    public void cancelSetup(BukkitCommandActor actor) {
        if (setupService.cancelSetup(actor.requirePlayer())) {
            actor.reply("&eCancelled setup");
        } else {
            actor.reply("&cYou don't have a setup in progress!");
        }
    }
}
