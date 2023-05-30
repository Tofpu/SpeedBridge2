package com.github.tofpu.speedbridge2;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JavaTest {

    @Test
    void empty_array_length() {
        Assertions.assertEquals(0, new String[0].length);
    }

    @Test
    void name() {
        final String[] array = new String[]{"hello"};

        Assertions.assertEquals(0, Arrays.copyOfRange(array, 1, array.length).length);
    }
}
