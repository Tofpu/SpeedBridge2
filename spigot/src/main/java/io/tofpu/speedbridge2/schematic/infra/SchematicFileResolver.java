package io.tofpu.speedbridge2.schematic.infra;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.io.FilenameUtils.getExtension;

public class SchematicFileResolver {
    private static final List<String> SCHEMATIC_TYPES = List.of("schematic", "schem");

    public File resolveSchematicFile(File directory, String name) {
        for (String schematicType : SCHEMATIC_TYPES) {
            File file = new File(directory, name + "." + schematicType);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    public Collection<String> schematicNames(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files).filter(file -> {
            String extension = getExtension(file.getName().toLowerCase(Locale.ENGLISH));
            return SCHEMATIC_TYPES.contains(extension);
        }).map(file -> {
            String fileName = file.getName();
            String extension = getExtension(file.getName());
            return fileName.replace("." + extension, "");
        }).toList();
    }
}
