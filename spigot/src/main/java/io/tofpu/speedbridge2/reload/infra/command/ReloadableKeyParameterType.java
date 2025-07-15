package io.tofpu.speedbridge2.reload.infra.command;

import io.tofpu.speedbridge2.reload.domain.ReloadRegistry;
import io.tofpu.speedbridge2.reload.domain.Reloadable;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.ArrayList;
import java.util.List;

class ReloadableKeyParameterType implements ParameterType<BukkitCommandActor, Reloadable.Key> {
    private final ReloadRegistry reloadRegistry;

    ReloadableKeyParameterType(ReloadRegistry reloadRegistry) {
        this.reloadRegistry = reloadRegistry;
    }

    @Override
    public Reloadable.Key parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> context) {
        String keyName = input.readString();
        if (keyName.equalsIgnoreCase(Reloadable.Key.ALL.name())) {
            return Reloadable.Key.ALL;
        }
        return Reloadable.Key.of(keyName);
    }

    @Override
    public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
        return context -> {
            List<String> list = new ArrayList<>();
            list.add(Reloadable.Key.ALL.name());
            reloadRegistry.all()
                    .stream().map(Reloadable::key)
                    .map(Reloadable.Key::name)
                    .forEachOrdered(list::add);
            return list;
        };
    }
}
