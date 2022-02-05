package io.tofpu.speedbridge2.command.subcommand;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Hidden;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class HelpCommandGenerator {
    private static final String HELP_BAR = "<yellow>" + MessageUtil.CHAT_BAR.substring(0,
            MessageUtil.CHAT_BAR.length() / 6);
    private static final String HELP_TITLE =
            HELP_BAR + "  " + "<gold" + "><bold> " + "SPEEDBRIDE V2</bold></gold>" + " " +
            HELP_BAR;
    private static Component helpMessageComponent = null;

    public static void generateHelpCommand(final @NotNull Plugin plugin) {
        final Method[] declaredMethods = SpeedBridgeCommand.class.getDeclaredMethods();

        final List<String> messages = new ArrayList<>();
        messages.add(HELP_TITLE);
        messages.add("");

        final String prefix =
                " <gold><bold>" + MessageUtil.Symbols.ARROW_RIGHT.getSymbol() + " ";
        final String infoPrefix = " <gold><bold>│ ";
        final String infoFormat = prefix + "</bold><yellow>%s: %s";

        messages.add(infoPrefix + "INFORMATION");
        messages.add(String.format(infoFormat, "Author", "Tofpu"));
        messages.add(String.format(infoFormat, "Version", plugin.getDescription()
                .getVersion()));

        messages.add("");

        final String format = prefix + "<reset><gold>/<yellow>%s <gray>-</gray> %s";

        messages.add(infoPrefix + "SPEEDBRIDGE COMMANDS");
        for (final Method method : declaredMethods) {
            final CommandMethod commandMethod = method.getAnnotation(CommandMethod.class);
            final CommandDescription commandDescription = method.getAnnotation(CommandDescription.class);
            if (commandMethod == null || method.isAnnotationPresent(Hidden.class)) {
                continue;
            }

            messages.add(String.format(format, commandMethod.value(), commandDescription.value()));
        }

        messages.add("");
        messages.add(infoPrefix + "NEED ASSISTANCE?");
        messages.add(prefix + "<yellow>Join our discord at <reset>https://discord" +
                     ".gg/rjks6D5Ynq");
        messages.add(MessageUtil.MENU_BAR);

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
