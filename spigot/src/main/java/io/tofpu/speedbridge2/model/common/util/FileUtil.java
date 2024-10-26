package io.tofpu.speedbridge2.model.common.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FileUtil {
    /**
     * Write a collection of strings to a file
     *
     * @param file             The file to write to.
     * @param append           If true, the file will be appended to if it already exists. If false,
     *                         the file will be overwritten if it already exists.
     * @param stringCollection The collection of strings to write to the file.
     */
    public static void write(final File file, final boolean append, final Collection<String> stringCollection) {
        try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8))) {
            for (final String message : stringCollection) {
                writer.write(message);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It reads the lines of a file into a list of strings
     *
     * @param file The file to read.
     * @return A collection of strings.
     */
    public static Collection<String> read(final File file) {
        final List<String> lines = new ArrayList<>();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                lines.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return lines;
    }
}
