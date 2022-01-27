package io.tofpu.speedbridge2.command.subcommand;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Hidden;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.MessageUtil;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class HelpCommandGenerator {
    private static final String HELP_BAR = MessageUtil.CHAT_BAR.substring(0, MessageUtil.CHAT_BAR
            .length() / 6);
    private static final String HELP_TITLE = "<yellow>" + HELP_BAR + "  " + "<gold" +
            "><bold> " + "SPEEDBRIDE COMMANDS</bold></gold>" + " " + HELP_BAR;
    private static Component helpMessageComponent = null;

    public static void generateHelpCommand() {
        final Method[] declaredMethods = SpeedBridgeCommand.class.getDeclaredMethods();

        final List<String> messages = new ArrayList<>();
        messages.add(HELP_TITLE);

        final String format =
                " <gold><bold>" + MessageUtil.Symbols.ARROW_RIGHT.getSymbol() + " " +
                        "<reset><gold>/<yellow>%s <gray>-</gray> %s";
        for (final Method method : declaredMethods) {
            final CommandMethod commandMethod = method.getAnnotation(CommandMethod.class);
            final CommandDescription commandDescription = method.getAnnotation(CommandDescription.class);
            if (commandMethod == null || method.isAnnotationPresent(Hidden.class)) {
                continue;
            }

            messages.add(String.format(format, commandMethod.value(), commandDescription.value()));
        }
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
