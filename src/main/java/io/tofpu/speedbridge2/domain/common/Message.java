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
    public static final String SCORE_TITLE_BAR = MessageUtil.CHAT_BAR.substring(0,
            MessageUtil.CHAT_BAR.length() / 6);
    @IgnoreMessage
    public final String style =
            "<gold>" + MessageUtil.Symbols.CLOCK.getSymbol() + "<yellow> ";
    @IgnoreMessage
    public final String secondStyle =
            "<gold>" + MessageUtil.Symbols.STAR.getSymbol() + "<yellow> ";
    @IgnoreMessage
    public final String error = "<red>" + MessageUtil.Symbols.WARNING.getSymbol() + " ";
    @IgnoreMessage
    public final String success =
            "<gold><bold>" + MessageUtil.Symbols.ARROW_RIGHT.getSymbol() +
            "</bold> <yellow>";
    public final String noArgument =
            success + "Type " + runCommand("/speedbridge help") + " for further " +
            "information.";
    public final String islandAlreadyExists = error + "Island %s already exists!";
    public final String islandHasBeenCreatedSchematic =
            success + "Island %s has been created with '%s' chosen as a " + "schematic!";
    public final String islandSetupNotification =
            success + "You can complete your %slot% island setup by running " +
            runCommand("/sb setup %slot%") + ", and setting up the spawn point.";
    public final String validSelect =
            success + "Island %s has selected" + " \"%s\" " + "as a " + "%s!";
    public final String unknownSchematic = error + "Schematic \"%s\" cannot be found";
    public final String invalidIslandArgument =
            error + "Invalid argument. Please choose a slot, or an island category" +
            ".\n" + error + "Alternatively, you could run the '" +
            runCommand("/randomjoin") + "' command.";
    public final String invalidIsland = error + "Island %s cannot be found!";
    public final String noAvailableIsland = error + "There is no island available " +
                                            "at the moment... please try again " +
                                            "later!";
    public final String alreadyInAIsland = error + "You're already on an island!";
    public final String inAGame = error + "You cannot execute this command while playing";
    public final String scoreTitle = "<yellow>" + SCORE_TITLE_BAR + "  " + "<gold><bold" +
                                     "> YOUR SCORES</bold></gold>" + " " +
                                     SCORE_TITLE_BAR;
    public final String joinedAnIsland = success + "You're now on island %s!";
    public final String leftAnIsland = success + "You left from island" + " %s!";
    public final String notInAIsland = error + "You're not on an island!";
    public final String deletedAnIsland = success + "Island %s has been deleted!";
    public final String emptySelect = error + "You haven't modified anything...";
    public final String reloaded = success + "The config has been reloaded!";
    public final String lobbySetLocation = success + "The lobby location has been set!";
    public final String emptySessionLeaderboard = "<strikethrough><gray>----";
    public final String timeStarted = style + "The timer is now ticking!";
    public final String scored =
            secondStyle + "You scored <yellow>%s</yellow> " + "seconds!";

    public final String lobbyMissing =
            error + "Incomplete setup. Please ensure to set up SpeedBridge's lobby to " +
            "complete the " + "process." + "\n<red>Type " +
            runCommand("/speedbridge setlobby") + " to set the " + "lobby.";

    public final String generalSetupIncomplete =
            error + "Incomplete setup. Please try again " + "later.";

    public final String startingSetupProcess =
            success + "You're now setting up %s " + "island.";

    public final String notInASetup = error + "You're not setting up anything.";
    public final String setSpawnPoint =
            success + "You have set the island's " + "spawnpoint.";

    public final String completeNotification =
            success + "You can complete the setup " + "by typing " +
            runCommand("/sb setup finish");

    public final String setupIncomplete = error + "The setup is incomplete. Please " +
                                          "ensure that the spawn point is set.";
    public final String setupComplete = success + "The setup is now complete.";

    public final String emptyScoreFormat = "";

    public final String invalidSpawnPoint =
            error + "The spawn point has to be set inside the regions.";

    public final String setupCancelled = success + "The setup has been cancelled.";

    public final String playerDoesntExist = error + "%s does not exist.";
    public final String playerWiped = success + "%s data has been wiped.";

    public final String somethingWentWrong =
            error + "Something went wrong... check " + "your console";
    public final String inASetup = error + "You're already in a setup.";

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
