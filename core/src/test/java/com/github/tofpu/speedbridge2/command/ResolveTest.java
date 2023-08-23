package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.resolve.argument.ArgumentResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResolveTest {
    @Test
    void name() {
        final ArgumentResolver resolver = new ArgumentResolver();
        resolver.register(Integer.class, (context) -> {
            try {
                int parseInt = Integer.parseInt(context.peek());
                context.poll();
                return parseInt;
            } catch (NumberFormatException e) {
                return null;
            }
        });

        final List<String> args = new ArrayList<>(Collections.singletonList("1, 2, 3"));
        
        resolver.resolve(Integer.class, args);
        Assertions.assertEquals(1, args.size());
    }
}
