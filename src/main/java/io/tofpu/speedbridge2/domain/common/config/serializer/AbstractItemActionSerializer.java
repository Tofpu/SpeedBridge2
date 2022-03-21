package io.tofpu.speedbridge2.domain.common.config.serializer;

import io.tofpu.speedbridge2.domain.common.umbrella.RunCommandItemAction;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class AbstractItemActionSerializer implements TypeSerializer<AbstractItemAction> {
    public static final AbstractItemActionSerializer INSTANCE = new AbstractItemActionSerializer();

    @Override
    public AbstractItemAction deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final String command = node.node("run_command").getString("");

        return new RunCommandItemAction(command, null);
    }

    @Override
    public void serialize(final Type type, @Nullable
    final AbstractItemAction obj, final ConfigurationNode node) throws SerializationException {
        if (!(obj instanceof RunCommandItemAction)) {
            return;
        }
        final RunCommandItemAction commandItemAction = (RunCommandItemAction) obj;

        // TODO: HAVE THIS CUSTOMIZABLE LATER
        node.node("type").set("PERFORM_COMMAND");
        node.node("run_command").set(commandItemAction.getCommand());
    }
}
