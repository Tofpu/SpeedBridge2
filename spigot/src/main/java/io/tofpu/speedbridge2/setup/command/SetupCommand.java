package io.tofpu.speedbridge2.setup.command;

import com.cryptomorin.xseries.XMaterial;
import io.tofpu.speedbridge2.command.ChildrenCommand;
import io.tofpu.speedbridge2.command.CommandHandler;
import io.tofpu.speedbridge2.command.CommandHandlerVisitor;
import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.setup.domain.SetupInfo;
import io.tofpu.speedbridge2.setup.infra.listener.indication.VirtualBorder;
import io.tofpu.speedbridge2.setup.service.SetupService;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.kyori.adventure.text.Component.text;

@Subcommand("setup")
public class SetupCommand extends ChildrenCommand implements CommandHandlerVisitor {
    private final SetupService setupService;

    public SetupCommand(SetupService setupService) {
        this.setupService = setupService;
    }

    @Subcommand("create")
    public void createSetup(BukkitCommandActor actor, int slot, Schematic schematic, @Flag("g") @Optional Group group) {
        if (setupService.createSetup(actor.requirePlayer(), new SetupInfo(slot, group, schematic))) {
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

    @Override
    public void visit(CommandHandler handler) {
        handler.addChildCommand(this);
        handler.addChildCommand(new Border());
    }

    @Subcommand("setup border")
    public static class Border extends ChildrenCommand {
        private final DataTypeFactory dataTypeFactory = new DataTypeFactory();

        @Subcommand("set particle")
        public void borderSetup(BukkitCommandActor actor, Particle particle) {
            Class<?> dataType = particle.getDataType();
            Object options = this.dataTypeFactory.createData(dataType);
            VirtualBorder.SettingsHolder.particle = particle;
            VirtualBorder.SettingsHolder.options = options;
            actor.reply(text("Set border particle to %s with data type %s".formatted(particle.name(), dataType.getSimpleName()), NamedTextColor.GREEN));
        }

        @Subcommand("set option dust")
        public void borderSetup(BukkitCommandActor actor, Color color, int size) {
            VirtualBorder.SettingsHolder.options = new Particle.DustOptions(color, size);
            actor.reply(text("Set border particle option to Dust with color %s and size %d".formatted(color.asRGB(), size), NamedTextColor.GREEN));
        }

        @Subcommand("set block")
        public void borderSetup(BukkitCommandActor actor, Material material) {
            VirtualBorder.SettingsHolder.options = material.createBlockData();
            actor.reply(text("Set border particle option to BlockData with material %s".formatted(material.name()), NamedTextColor.GREEN));
        }

        @Subcommand("set count")
        public void borderSetup(BukkitCommandActor actor, int amount) {
            VirtualBorder.SettingsHolder.count = amount;
            actor.reply(text("Set border particle count to %d".formatted(amount), NamedTextColor.GREEN));
        }

        static class DataTypeFactory {
            private final Map<Class<?>, Supplier<Object>> dataTypes = new HashMap<>();

            public DataTypeFactory() {
                dataTypes.put(Particle.DustOptions.class, () -> new Particle.DustOptions(Color.RED, 1));
                dataTypes.put(BlockData.class, () -> XMaterial.RED_DYE.get().createBlockData());
            }

            public Object createData(Class<?> dataType) {
                if (dataType == Void.class) {
                    return null;
                }
                Supplier<Object> objectSupplier = dataTypes.get(dataType);
                if (objectSupplier == null) {
                    throw new CommandErrorException("Unsupported particle with data type %s".formatted(dataType.getSimpleName()));
                }
                return objectSupplier.get();
            }
        }
    }
}
