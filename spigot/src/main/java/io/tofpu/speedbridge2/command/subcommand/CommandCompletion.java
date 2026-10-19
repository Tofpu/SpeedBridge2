package io.tofpu.speedbridge2.command.subcommand;

import io.tofpu.speedbridge2.command.condition.annotation.MaterialType;
import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.util.material.MultiMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class CommandCompletion {
    private final IslandService islandService;

    public CommandCompletion(final IslandService islandService) {
        this.islandService = islandService;
    }

    public @NotNull List<String> islands(final List<String> args, final CommandActor actor,
                                         final ExecutableCommand command) {
        final List<String> suggestions = new ArrayList<>();

        for (final Integer integer : islandService.getIntegerIslands()) {
            suggestions.add(String.valueOf(integer));
        }
        System.out.println("CommandCompletion#islands: suggestions - " + suggestions);

        return suggestions;
    }

    public @NotNull Collection<String> materials(final List<String> args, final CommandActor actor,
                                                 final ExecutableCommand command) {
        AtomicReference<MaterialType> annotationRef = new AtomicReference<>();
        boolean anyMatch = command.getParameters().stream().anyMatch(commandParameter -> {
            Parameter javaParameter = commandParameter.getJavaParameter();
            boolean present = javaParameter.isAnnotationPresent(MaterialType.class);
            if (present) {
                annotationRef.set(javaParameter.getAnnotation(MaterialType.class));
            }
            return present;
        });

        MaterialType annotation = annotationRef.get();
        if (!anyMatch) {
            return MultiMaterial.materials();
        }
        return MultiMaterial.materials(annotation.category());
    }

    public Collection<String> players(@NotNull List<String> args, @NotNull CommandActor sender, @NotNull ExecutableCommand executableCommand) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !((BukkitCommandActor) sender).isPlayer()
                        || ((BukkitCommandActor) sender).requirePlayer().canSee(player)
                        && !((BukkitCommandActor) sender).getSender().equals(player)
                ).map(HumanEntity::getName)
                .collect(Collectors.toList());

    }
}
