package io.tofpu.speedbridge2.domain.common;

import io.tofpu.speedbridge2.domain.common.util.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class Message {
    private static final @NotNull Map<String, Field> FIELD_MAP = new ConcurrentHashMap<>();
    public static final @NotNull Message INSTANCE = new Message();

    @IgnoreMessage
    public final String ERROR = "<red>" + MessageUtil.Symbols.WARNING.getSymbol() + " ";
    @IgnoreMessage
    public final String SUCCESS =
            "<gold><bold>" + MessageUtil.Symbols.ARROW_RIGHT.getSymbol() +
            "</bold> <yellow>";

    public final String NO_ARGUMENT =
            SUCCESS + "Type " + runCommand("/speedbridge help") + " for further " + "information.";

    public final String ISLAND_ALREADY_EXISTS = ERROR + "Island %s already exists!";

    public final String ISLAND_HAS_BEEN_CREATED =
            SUCCESS + "Island %s has been " + "created!";
    public final String ISLAND_HAS_BEEN_CREATED_SCHEMATIC =
            ISLAND_HAS_BEEN_CREATED.replaceAll("!", "") + " with \"%s\" chosen as a " +
            "schematic!";

    public final String VALID_SELECT =
            SUCCESS + "Island %s has selected" + " \"%s\" " + "as a " + "%s!";

    public final String UNKNOWN_SCHEMATIC = ERROR + "Schematic \"%s\" cannot be found";

    public final String INVALID_ISLAND_ARGUMENT =
            ERROR + "Invalid argument. Please choose a slot, or an island category" +
            ".\n" + ERROR + "Alternatively, you could run the '" + runCommand("/randomjoin") + "' command.";
    public final String INVALID_ISLAND = ERROR + "Island %s cannot be found!";
    public final String NO_AVAILABLE_ISLAND = ERROR + "There is no island available " +
                                              "at the moment... please try again " +
                                              "later!";

    public final String ALREADY_IN_A_ISLAND =
            ERROR + "You're already on an " + "island!";

    @IgnoreMessage
    public final String SCORE_TITLE_BAR = MessageUtil.CHAT_BAR.substring(0,
            MessageUtil.CHAT_BAR.length() / 6);

    public final String SCORE_TITLE =
            "<yellow>" + SCORE_TITLE_BAR + "  " + "<gold><bold" +
            "> YOUR SCORES</bold></gold>" + " " + SCORE_TITLE_BAR;

    public final String JOINED_AN_ISLAND = SUCCESS + "You're now on island %s!";
    public final String LEFT_AN_ISLAND = SUCCESS + "You left from island" + " %s!";
    public final String NOT_IN_A_ISLAND = ERROR + "You're not on an island!";

    public final String DELETED_AN_ISLAND = SUCCESS + "Island %s has been deleted!";

    public final String EMPTY_SELECT = ERROR + "You haven't modified anything...";

    public final String RELOADED = SUCCESS + "The config has been reloaded!";

    public final String LOBBY_SET_LOCATION = SUCCESS + "The lobby location has been set!";

    public final String EMPTY_SESSION_LEADERBOARD = "<strikethrough><gray>----";

    @IgnoreMessage
    public final String STYLE =
            "<gold>" + MessageUtil.Symbols.CLOCK.getSymbol() + "<yellow> ";
    @IgnoreMessage
    public final String SECOND_STYLE =
            "<gold>" + MessageUtil.Symbols.STAR.getSymbol() + "<yellow> ";

    public final String TIME_STARTED = STYLE + "The timer is now ticking!";
    public final String SCORED =
            SECOND_STYLE + "You scored <yellow>%s</yellow> " + "seconds!";

    public final String LOBBY_MISSING =
            ERROR + "Incomplete setup. Please ensure to set up SpeedBridge's lobby to " +
            "complete the " + "process." +
            "\n<red>Type " + runCommand("/speedbridge setlobby") + " to set the " + "lobby.";

    public final String GENERAL_SETUP_INCOMPLETE =
            ERROR + "Incomplete setup. Please try again " + "later.";
    public final String STARTING_SETUP_PROCESS =
            SUCCESS + "You're now setting up %s " + "island";
    public final String NOT_IN_A_SETUP = ERROR + "You're not setting up anything.";
    public final String SET_SPAWN_POINT =
            SUCCESS + "You have set the island's " + "spawnpoint.";
    public final String COMPLETE_NOTIFICATION =
            SUCCESS + "You can complete the setup " + "by typing " +
            runCommand("/sb setup finish");
    public final String SETUP_INCOMPLETE = ERROR + "The setup is incomplete. Please " +
                                           "ensure that the spawnpoint is set.";
    public final String SETUP_COMPLETE = SUCCESS + "The setup is now complete.";

    public final String EMPTY_SCORE_FORMAT = "";
    public final String INVALID_SPAWN_POINT =
            ERROR + "The spawnpoint has to be set " + "inside the regions.";
    public final String SETUP_CANCELLED = SUCCESS + "The setup has been cancelled.";

    private static String runCommand(final String command) {
        return "<hover:show_text:'<yellow>Click to run " +
               "%command%'><click:run_command:'%command%'>%command%".replace(
                       "%command" + "%", command);
    }

    public static @NotNull CompletableFuture<Void> load(final File directory) {
        return CompletableFuture.runAsync(() -> {
            if (FIELD_MAP.isEmpty()) {
                for (final Field field : Message.class.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()) ||
                        field.isAnnotationPresent(IgnoreMessage.class)) {
                        continue;
                    }

                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    FIELD_MAP.put(field.getName(), field);
                }
                BridgeUtil.debug(String.valueOf(FIELD_MAP));
            }

            final File messageFile = new File(directory, "messages.yml");
            final boolean messageFileExists = messageFile.exists();

            BridgeUtil.debug(String.valueOf(FIELD_MAP));

            if (!messageFileExists) {
                FileUtil.write(messageFile, false, ReflectionUtil.toString(FIELD_MAP));
                return;
            }

            final List<String> fieldList = new ArrayList<>();
            for (final String fileMessage : FileUtil.read(messageFile)) {
                final String[] args = fileMessage.split(":");

                final String fieldName = args[0];
                final String message = substring(args, 1).replaceFirst(" ", "")
                        .replace("\\n", "\n");

                final Field field = FIELD_MAP.get(fieldName);
                if (field == null) {
                    continue;
                }

                fieldList.add(fieldName);
                try {
                    field.set(INSTANCE, message);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }

            final Map<String, Field> missingFields = new HashMap<>();
            for (final Map.Entry<String, Field> entry : FIELD_MAP.entrySet()) {
                if (fieldList.contains(entry.getKey())) {
                    continue;
                }

                missingFields.put(entry.getKey(), entry.getValue());
            }

            FileUtil.write(messageFile, true, ReflectionUtil.toString(missingFields));
        });
    }

    public static String substring(final String[] array, final int startIndex) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < array.length; i++) {
            if (i != startIndex) {
                builder.append(":");
            }
            builder.append(array[i]);
        }
        return builder.toString();
    }
}
