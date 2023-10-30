package com.github.tofpu.speedbridge2;

import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum BukkitMessages implements ConfigurableMessage {
    PERSONAL_BEST("personal_best", ChatColor.YELLOW + "A best score of %s on %s island!".replace("%s", BukkitMessages.wrapValueWithYellow())),
    PERSONAL_BEST_GLOBAL_TITLE("personal_best_global_title", ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "Best scores on all islands"),
    PERSONAL_BEST_GLOBAL_BODY("personal_best_global_body", ChatColor.DARK_GRAY + String.valueOf(ChatColor.BOLD) + "| " + ChatColor.DARK_GRAY + "-> %s on island %s".replace("%s", wrapValueWithYellow())),
    NO_PERSONAL_BEST("no_personal_best", ChatColor.RED + "You have not scored anything yet."),
    NO_PERSONAL_BEST_ON_ISLAND("no_personal_best_on_island", ChatColor.RED + "You have not scored on %s island yet.".replace("%s", wrapValueWithRed())),
    SUCCESSFUL_RELOAD("reload", ChatColor.YELLOW + "Messages was reloaded."), RELOAD_ERROR("failed_to_reload", ChatColor.RED + "An error has occurred while reloading the messages!");

    @NotNull
    private static String wrapValueWithRed() {
        return BukkitMessages.wrapValue(ChatColor.RED);
    }

    private final String key;

    private String defaultMessage;
    BukkitMessages(String key, String defaultMessage) {
        this.key = key;
        this.defaultMessage = defaultMessage;
    }

    @NotNull
    private static String wrapValueWithYellow() {
        return wrapValue(ChatColor.YELLOW);
    }

    private static String wrapValue(ChatColor originalColor) {
        return ChatColor.WHITE + "%s" + originalColor;
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