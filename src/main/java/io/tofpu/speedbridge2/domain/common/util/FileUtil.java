package io.tofpu.speedbridge2.domain.common.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FileUtil {
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
