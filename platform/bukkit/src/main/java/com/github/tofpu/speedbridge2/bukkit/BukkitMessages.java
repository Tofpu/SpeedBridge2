package com.github.tofpu.speedbridge2.bukkit;

import com.github.tofpu.speedbridge2.object.player.ConfigurableMessage;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum BukkitMessages implements ConfigurableMessage {
    LEFT_GAME("left_game", ChatColor.GREEN + "You left the game!"),
    LOBBY_SET("lobby_set", ChatColor.GREEN + "Lobby location was successfully set."),
    NOT_IN_GAME("not_in_game", ChatColor.RED + "You must be in a game to leave."),
    LOBBY_NOT_AVAILABLE("lobby_not_available", ChatColor.RED + "Lobby is not currently available!"),
    LOBBY_UNKNOWN("lobby_unknown", ChatColor.RED + "Unknown island: %s".replace("%s", BukkitMessages.wrapValueWithYellow())),
    GAME_SETUP_CANCELLED("game_setup_cancelled", ChatColor.GREEN + "You have successfully cancelled your setup of %s".replace("%s", BukkitMessages.wrapValueWithYellow())),
    GAME_SETUP_PLAYER_MISSING("game_setup_player_missing", ChatColor.RED + "You must be in a setup to leave."),
    GAME_SETUP_PLAYER_BUSY("game_setup_player_busy", ChatColor.RED + "You are already setting up an island."),
    GAME_SETUP_CREATED("game_setup_created", ChatColor.YELLOW + "You are now setting up %s island.".replace("%s", BukkitMessages.wrapValueWithYellow())),
    GAME_SETUP_SET_SPAWNPOINT("game_setup_set_spawnpoint", ChatColor.YELLOW + "You have set the spawn-point of the island!"),
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