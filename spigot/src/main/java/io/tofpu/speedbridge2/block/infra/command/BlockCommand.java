package io.tofpu.speedbridge2.block.infra.command;

import io.tofpu.speedbridge2.block.domain.Block;
import io.tofpu.speedbridge2.block.service.BlockMenuService;
import io.tofpu.speedbridge2.block.service.BlockService;
import io.tofpu.speedbridge2.command.ChildrenCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.text;

@Subcommand("block")
public class BlockCommand extends ChildrenCommand {
    private final BlockService blockService;
    private final BlockMenuService blockMenuService;

    public BlockCommand(BlockService blockService, BlockMenuService blockMenuService) {
        this.blockService = blockService;
        this.blockMenuService = blockMenuService;
    }

    @Subcommand("menu")
    @CommandPermission("speedbridge2.block.menu")
    public void blockMenu(CommandSender sender, @Default("me") @Optional Player target) {
        blockMenuService.open(target);
    }

    @Subcommand("set")
    @CommandPermission("speedbridge2.block.set")
    public void blockSet(CommandSender sender, Block block, @Default("me") Player target) {
        blockService.setBlock(target.getUniqueId(), block.id());
        sender.sendMessage(
                text(String.format("Block set to %s for player %s", block.id(), target.getName()))
        );
    }

    @Subcommand("get")
    @CommandPermission("speedbridge2.block.get")
    public void blockGet(CommandSender sender, @Default("me") Player target) {
        String blockId = blockService.blockId(target.getUniqueId());
        if (blockId == null) {
            sender.sendMessage(text(String.format("No block set for player %s", target.getName())));
        } else {
            sender.sendMessage(
                    text(String.format("Block for player %s is %s", target.getName(), blockId))
            );
        }
    }
}
