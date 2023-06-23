package com.github.tofpu.speedbridge2.command;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

    @Test
    void args_size_change() throws InvocationTargetException, IllegalAccessException {
        DemoClass demoClass = spy(new DemoClass());

        Method print = demoClass.getClass().getDeclaredMethods()[0];
        print.setAccessible(true);

        print.invoke(demoClass, "yo");
        verify(demoClass, times(1)).print(eq("yo"));
    }

    private static class DemoClass {

        void print(String message) {
            System.out.println(message);
        }
    }
}
