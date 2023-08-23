package com.github.tofpu.speedbridge2.command.resolve.argument;

import java.util.Arrays;

public class BadArgumentException extends RuntimeException {
    public BadArgumentException(Object[] expected, Object[] actual) {
        super(String.format("Bad arguments! Expected %s but received %s", Arrays.toString(expected), Arrays.toString(actual)));
    }
}
