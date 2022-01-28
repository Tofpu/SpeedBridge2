package io.tofpu.speedbridge2.domain.common.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public final class FileUtil {
    public static void write(final File file, final boolean append, final Collection<String> strings) {
        try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8))) {
            for (final String message : strings) {
                writer.write(message);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
