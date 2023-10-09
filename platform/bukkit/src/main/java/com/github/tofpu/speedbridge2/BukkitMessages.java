package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum BukkitMessages implements ConfigurableMessage {
    PERSONAL_BEST("personal_best", ChatColor.YELLOW + "You scored a personal best score of %s on %s island!".replace("%s", BukkitMessages.wrapValue())),
    NO_PERSONAL_BEST("no_personal_best", ChatColor.RED + "You have not scored anything yet."),
    SUCCESSFUL_RELOAD("reload", ChatColor.YELLOW + "Messages was reloaded."), RELOAD_ERROR("failed_to_reload", ChatColor.RED + "An error has occurred while reloading the messages!");

    private final String key;
    private String defaultMessage;

    BukkitMessages(String key, String defaultMessage) {
        this.key = key;
        this.defaultMessage = defaultMessage;
    }

    @NotNull
    private static String wrapValue() {
        return ChatColor.WHITE + "%s" + ChatColor.YELLOW;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }

    @Override
    public void setMessage(String content) {
        defaultMessage = content;
    }
}