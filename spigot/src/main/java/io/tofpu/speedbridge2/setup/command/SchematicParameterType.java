package io.tofpu.speedbridge2.setup.command;

import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

public class SchematicParameterType implements ParameterType<BukkitCommandActor, Schematic> {
    private final SchematicHandler schematicHandler;

    public SchematicParameterType(SchematicHandler schematicHandler) {
        this.schematicHandler = schematicHandler;
    }

    @Override
    public Schematic parse(
            @NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> context) {
        String schematicName = input.readString();
        Schematic schematic = schematicHandler.resolveSchematic(schematicName);
        if (schematic == null) {
            throw new CommandErrorException("&cCould not find schematic with name " + schematicName);
        }
        return schematic;
    }

    @Override
    public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
        return context -> schematicHandler.schematicNames();
    }
}
