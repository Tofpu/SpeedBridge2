package io.tofpu.speedbridge2.command.subcommand;

import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.object.extra.CommonBridgePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public final class HelpCommandGenerator {
    private static final String FIRST_CHARACTER = "<dark_gray>| ";
    private static final String TITLE = FIRST_CHARACTER + "- <white><bold>%s</bold>";
    private static final String SUBTITLE = FIRST_CHARACTER + "-- <yellow>%s";
    private static final String PAIR_FORMAT = String.format(SUBTITLE, "%s: <white>%s");

    private static final String HEADER = FIRST_CHARACTER +
                                         "<<gold>-</gold>> <yellow><bold>SpeedBridge <white>V2</bold>";
    private static final String COMMAND_FORMAT = String.format(SUBTITLE, "/sb %s %s" +
                                                                         "<dark_gray>- <white>%s");

    private static Component helpMessageComponent = null;

    public static void generateHelpCommand(final @NotNull Plugin plugin) {
        final Method[] declaredMethods = SpeedBridgeCommand.class.getDeclaredMethods();

        final List<String> messages = new ArrayList<>();
        messages.add(HEADER);
        messages.add("");

        messages.add(String.format(TITLE, "Information"));
        messages.add(String.format(PAIR_FORMAT, "Author", "Tofpu"));
        messages.add(String.format(PAIR_FORMAT, "Version", plugin.getDescription()
                .getVersion()));
        messages.add("");

        messages.add(String.format(TITLE, "Commands"));
        for (final Method method : declaredMethods) {
            final Subcommand commandMethod = method.getAnnotation(Subcommand.class);
            final Description commandDescription = method.getAnnotation(Description.class);
            if (commandMethod == null) {
                continue;
            }
            final String usage = generateUsageOfMethod(commandMethod, method);

            messages.add(String.format(COMMAND_FORMAT, commandMethod.value()[0],
                    usage, commandDescription.value()));
        }

        messages.add("");
        messages.add(String.format(TITLE, "Support"));
        messages.add(String.format(PAIR_FORMAT, "Discord",
                "<click:OPEN_URL:https://tofpu.me/discord>tofpu.me/discord"));

        final StringBuilder builder = new StringBuilder();
        for (final String message : messages) {
            if (builder.length() != 0) {
                builder.append("\n");
            }
            builder.append(message);
        }

        helpMessageComponent = BridgeUtil.translateMiniMessage(builder.toString());
    }

    public static String generateUsageOfMethod(final Subcommand subcommand,
            final Method method) {
        final StringBuilder builder = new StringBuilder();

        if (method.isAnnotationPresent(Usage.class)) {
            return method.getAnnotation(Usage.class).value().replace(subcommand.value()[0] + " ", "") + " ";
        }

        for (final Parameter parameter : method.getParameters()) {
            if (CommonBridgePlayer.class.isAssignableFrom(parameter.getType())) {
                continue;
            }

            if (builder.length() != 0) {
                builder.append(" ");
            }

            final String name;
            switch (parameter.getType().getSimpleName()) {
                case "Island":
                    name = "slot";
                    break;
                default:
                    name = parameter.getName();
            }

            String startingTag = "<";
            String closingTag = ">";
            if (parameter.isAnnotationPresent(Optional.class)) {
                startingTag = "[";
                closingTag = "]";
            }

            String flag = "";
            if (parameter.isAnnotationPresent(Flag.class)) {
                final Flag flagAnnotation = parameter.getAnnotation(Flag.class);
                flag = "-" + flagAnnotation.value() + " ";
            }

            builder.append(startingTag).append(flag).append(name).append(closingTag).append(" ");
        }

        return builder.toString();
    }

    public static void showHelpMessage(final CommandSender sender) {
        BridgeUtil.sendMessage(sender, helpMessageComponent);
    }
}
