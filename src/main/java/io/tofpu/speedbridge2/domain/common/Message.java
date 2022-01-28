package io.tofpu.speedbridge2.domain.common;

import io.tofpu.speedbridge2.domain.common.util.FileUtil;
import io.tofpu.speedbridge2.domain.common.util.IgnoreMessage;
import io.tofpu.speedbridge2.domain.common.util.MessageUtil;
import io.tofpu.speedbridge2.domain.common.util.ReflectionUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class Message {
    private static final Map<String, Field> FIELD_MAP = new ConcurrentHashMap<>();
    public static final Message INSTANCE = new Message();

    @IgnoreMessage
    public final String ERROR = "<red>" + MessageUtil.Symbols.WARNING.getSymbol() + " ";
    @IgnoreMessage
    public final String SUCCESS =
            "<gold><bold>" + MessageUtil.Symbols.CROSS.getSymbol() + "</bold> <yellow>";

    public final String ISLAND_ALREADY_EXISTS = ERROR + "Island %s already exists!";

    public final String ISLAND_HAS_BEEN_CREATED =
            SUCCESS + "Island %s has been " + "created!";
    public final String ISLAND_HAS_BEEN_CREATED_SCHEMATIC =
            ISLAND_HAS_BEEN_CREATED + " with %s chosen as a schematic!";

    public final String VALID_SELECT =
            SUCCESS + "Island %s has selected" + " \"%s\" " + "as a " + "%s!";

    public final String INVALID_SCHEMATIC =
            ERROR + "\"%s\" cannot be found as a schematic";

    public final String INVALID_ISLAND_ARGUMENT =
            ERROR + "You have to insert a " + "slot or a category" +
            ". or you could run the /randomjoin command.";
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

    public final String JOINED_AN_ISLAND = SUCCESS + "You joined the " + "island %s!";
    public final String LEFT_AN_ISLAND = SUCCESS + "You left from island" + " %s!";
    public final String NOT_IN_A_ISLAND = ERROR + "You're not on an island!";

    public final String DELETED_AN_ISLAND = SUCCESS + "Island %s has been deleted!";

    public final String EMPTY_SELECT = ERROR + "You haven't modified anything...";

    public final String RELOADED = SUCCESS + "The config has been reloaded!";

    public static CompletableFuture<Void> load(final File directory) {
        final File messageFile = new File(directory, "messages.yml");
        final boolean fileExists = messageFile.exists();

        System.out.println(FIELD_MAP);

        final Class<Message> messageClass = Message.class;
        if (!fileExists) {
            FileUtil.write(messageFile, false, ReflectionUtil.toString(FIELD_MAP, messageClass));
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            final List<String> fieldList = new ArrayList<>();
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(messageFile), "UTF8"))) {
                while (reader.ready()) {
                    final String input = reader.readLine();
                    final String[] args = input.split(":");

                    final String fieldName = args[0];
                    final String message = args[1].replaceFirst(" ", "");

                    final Field field = FIELD_MAP.get(fieldName);
                    if (field == null) {
                        continue;
                    }

                    fieldList.add(fieldName);
                    field.set(INSTANCE, message);
                }
            } catch (IOException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }

            final Map<String, Field> missingFields = new HashMap<>();
            for (final Map.Entry<String, Field> entry : FIELD_MAP.entrySet()) {
                if (fieldList.contains(entry.getKey())) {
                    continue;
                }

                missingFields.put(entry.getKey(), entry.getValue());
            }

            FileUtil.write(messageFile, true, ReflectionUtil.toString(missingFields, messageClass));
        });
    }

    // from listeners

    @IgnoreMessage
    public final String STYLE =
            "<gold>" + MessageUtil.Symbols.CLOCK.getSymbol() + "<yellow> ";
    @IgnoreMessage
    public final String SECOND_STYLE =
            "<gold>" + MessageUtil.Symbols.STAR.getSymbol() + "<yellow> ";

    public final String TIME_STARTED = STYLE + "The timer is now ticking!";
    public final String SCORED =
            SECOND_STYLE + "You scored <yellow>%s</yellow> " + "seconds!";

    // placeholders

    public final String BEST_SCORE_FORMAT = SUCCESS + "Your personal score is %s seconds";

    private Message() {
        CompletableFuture.runAsync(() -> {
            for (final Field field : Message.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(IgnoreMessage.class)) {
                    continue;
                }

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                FIELD_MAP.put(field.getName(), field);
            }
            System.out.println(FIELD_MAP);
        });
    }
}
