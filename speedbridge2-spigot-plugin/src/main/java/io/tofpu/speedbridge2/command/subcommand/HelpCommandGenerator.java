package io.tofpu.speedbridge2.command.subcommand;

import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterHolder;
import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterHolderImpl;
import io.tofpu.speedbridge2.model.common.presenter.type.MessagePairPresenter;
import io.tofpu.speedbridge2.model.common.presenter.type.MessageTreePresenter;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.object.CommonBridgePlayer;
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

public final class HelpCommandGenerator {
    private static final String TITLE = "<white><bold>%s</bold>";
    private static final String KEY_STYLE = "<yellow>%s";
    private static final String VALUE_STYLE = "<white>%s";
    private static final String COMMAND_STYLE = "<yellow>/sb %s %s<dark_gray>- <white>%s";
    private static final String DISCORD_LINK = "https://discord.gg/cDQjsHugPw";

    private static Component helpMessageComponent = null;

    public static void generateHelpCommand(final @NotNull Plugin plugin) {
        final Method[] declaredMethods = SpeedBridgeCommand.class.getDeclaredMethods();

        final MessagePresenterHolder holder = new MessagePresenterHolderImpl("<yellow><bold>SpeedBridge <white>v2");

        holder.append(() -> {
            final MessagePairPresenter.Builder builder = new MessagePairPresenter.Builder();

            builder.title(String.format(TITLE, "Information"));
            builder.pair(String.format(KEY_STYLE, "Author"), String.format(VALUE_STYLE, "Tofpu"))
                    .pair(String.format(KEY_STYLE, "Version"), String.format(VALUE_STYLE, plugin.getDescription()
                            .getVersion()));

            return builder.build();
        });

        holder.append(() -> {
            final MessageTreePresenter.Builder builder = new MessageTreePresenter.Builder();

            builder.title(String.format(TITLE, "Commands"));
            for (final Method method : declaredMethods) {
                final Subcommand commandMethod = method.getAnnotation(Subcommand.class);
                final Description commandDescription = method.getAnnotation(Description.class);
                if (commandMethod == null) {
                    continue;
                }
                final String usage = generateUsageOfMethod(commandMethod, method);

                builder.message(String.format(COMMAND_STYLE, commandMethod.value()[0], usage, commandDescription.value()));
            }
            return builder.build();
        });

        holder.append(() -> {
            final MessagePairPresenter.Builder builder = new MessagePairPresenter.Builder();

            builder.title(String.format(TITLE, "Support"))
                    .pair(String.format(KEY_STYLE, "Discord"), String.format(VALUE_STYLE,
                            "<click:OPEN_URL:" + DISCORD_LINK + ">" + DISCORD_LINK));

            return builder.build();
        });

        helpMessageComponent = BridgeUtil.translateMiniMessage(holder.getResult());
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
