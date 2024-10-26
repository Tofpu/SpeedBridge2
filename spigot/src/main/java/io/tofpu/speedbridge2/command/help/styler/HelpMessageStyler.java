package io.tofpu.speedbridge2.command.help.styler;

import io.tofpu.speedbridge2.command.help.ParentCommand;
import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterHolder;
import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterHolderImpl;
import io.tofpu.speedbridge2.model.common.presenter.type.MessagePairPresenter;
import io.tofpu.speedbridge2.model.common.presenter.type.MessageTreePresenter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class HelpMessageStyler {

    private static final String TITLE = "<white><bold>%s</bold>";
    private static final String KEY_STYLE = "<yellow>%s";
    private static final String VALUE_STYLE = "<white>%s";
    private static final String COMMAND_STYLE = "<yellow>/%s %s <dark_gray>- <white>%s";
    private static final String DISCORD_LINK = "https://discord.gg/cDQjsHugPw";

    public @NotNull MessagePresenterHolder create(Plugin plugin, ParentCommand parentCommand) {
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

            parentCommand.commands().forEach(command -> {
                String path = command.getPath().toRealString();
                String usage = command.getUsage();
                String description = command.getDescription();
                if (description == null) {
                    description = "No description provided";
                }

                String message = String.format(COMMAND_STYLE, path, usage, description);
                message = StringUtils.normalizeSpace(message);
                builder.message(message);
            });

            return builder.build();
        });

        holder.append(() -> {
            final MessagePairPresenter.Builder builder = new MessagePairPresenter.Builder();

            builder.title(String.format(TITLE, "Support"))
                    .pair(String.format(KEY_STYLE, "Discord"), String.format(VALUE_STYLE,
                            "<click:OPEN_URL:" + DISCORD_LINK + ">" + DISCORD_LINK));

            return builder.build();
        });
        return holder;
    }
}
