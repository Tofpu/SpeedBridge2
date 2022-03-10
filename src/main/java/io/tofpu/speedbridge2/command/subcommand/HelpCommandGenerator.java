package io.tofpu.speedbridge2.command.subcommand;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Hidden;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class HelpCommandGenerator {
    private static final String FIRST_CHARACTER = "<dark_gray>| ";
    private static final String TITLE = FIRST_CHARACTER + "- <white><bold>%s</bold>";
    private static final String SUBTITLE = FIRST_CHARACTER + "-- <yellow>%s";
    private static final String PAIR_FORMAT = String.format(SUBTITLE, "%s: <white>%s");

    private static final String HEADER = FIRST_CHARACTER +
                                         "<<gold>-</gold>> <yellow><bold>SpeedBridge <white>V2</bold>";
    private static final String COMMAND_FORMAT = String.format(SUBTITLE, "/%s " +
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
            final CommandMethod commandMethod = method.getAnnotation(CommandMethod.class);
            final CommandDescription commandDescription = method.getAnnotation(CommandDescription.class);
            if (commandMethod == null || method.isAnnotationPresent(Hidden.class)) {
                continue;
            }

            messages.add(String.format(COMMAND_FORMAT, commandMethod.value()
                    .replace("speedbridge|sb", "sb"), commandDescription.value()));
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

    public static void showHelpMessage(final CommandSender sender) {
        BridgeUtil.sendMessage(sender, helpMessageComponent);
    }
}
