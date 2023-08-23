package com.github.tofpu.speedbridge2.command.resolve.argument;

import com.github.tofpu.speedbridge2.command.annontation.Join;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodWrapper;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodWrapper.Parameter;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;

public class ArgumentResolver {
    private final Map<Class<?>, Function<ResolverContext, Object>> resolveMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> type, Function<ResolverContext, T> inputConsumer) {
        this.resolveMap.put(type, (Function<ResolverContext, Object>) inputConsumer);
    }

    public Object[] resolve(MethodWrapper methodWrapper, String[] args, Object actor) {
        final Queue<String> queue = new LinkedList<>();
        Collections.addAll(queue, args);
        return resolve(actor, methodWrapper, queue);
    }

    private Object[] resolve(Object actor, MethodWrapper methodWrapper, Queue<String> inputArgs) {
        final Queue<Object> objectValue = new LinkedList<>();

        if (methodWrapper.parameterCount() >= 1) {
            Parameter firstParameter = methodWrapper.parameters()[0];
            if (firstParameter.type().isInstance(actor)) {
                objectValue.add(actor);
            }
        }

        int i = -1;
        for (Parameter parameter : methodWrapper.parameters()) {
            i++;
            if (0 == inputArgs.size()) break;

            Class<?> type = parameter.type();
            System.out.println(type);

            if (i == 0 && type.isInstance(actor)) continue;

            Object arg = inputArgs.peek();
            System.out.println("isInstance: " + type.isInstance(arg));
            if (type.isInstance(arg)) {
                objectValue.add(arg);
                continue;
            }

            System.out.println("Attempting to resolve to " + type.getSimpleName());
            Object resolvedInput = resolve(type, (List<String>) inputArgs);
            if (resolvedInput == null) {
                throw new RuntimeException(String.format("Unable to resolve input %s to %s", arg, type.getSimpleName()));
            }

            System.out.println("Resolved to " + type.getSimpleName() + " (" + resolvedInput + ")");
            objectValue.add(resolvedInput);
        }

        if (inputArgs.size() != 0 && methodWrapper.parameterCount() != 0 && contain(
            methodWrapper, Join.class)) {
            System.out.println(
                "Found @Join annotation, joining given args: " + objectValue);
//            args = new Object[]{args};
        }
        System.out.println("Result: " + objectValue);
        return objectValue.toArray(new Object[0]);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type, List<String> args) {
        Function<ResolverContext, Object> resolverFunction = this.resolveMap.get(type);
        if (resolverFunction == null) return null;

        ResolverContext resolverContext = new ResolverContext(args);
//        System.out.println("from " + args.length + " to " + resolverContext.arguments().length);
//        args = resolverContext.arguments();
        return (T) resolverFunction.apply(resolverContext);
    }

//    private Object[] resolve(String[] args) {
//        final Object[] resolvedArgs = new Object[args.length];
//
//        for (int i = 0; i < args.length; i++) {
//            final String arg = args[i];
//
//            for (Map.Entry<Class<?>, Function<String, Object>> entry : resolveMap.entrySet()) {
//                Object apply = entry.getValue().apply(arg);
//                if (apply != null) {
//                    resolvedArgs[i] = apply;
//                    break;
//                }
//            }
//        }
//        return resolvedArgs;
//    }

    private boolean contain(MethodWrapper methodWrapper,
                            Class<? extends Annotation> clazz) {
        for (Parameter parameter : methodWrapper.parameters()) {
            if (parameter.isAnnotationPresent(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static class ResolverContext {
        private final List<String> original;
        private final Queue<String> queue;

        public ResolverContext(List<String> arguments) {
            this.original = arguments;
            this.queue = new LinkedList<>(arguments);
        }

        public String peek() {
            return queue.peek();
        }

        public String poll() {
            String poll = queue.poll();
            this.original.remove(poll);
            return poll;
        }

        @SuppressWarnings("unchecked")
        public List<String> original() {
            return original;
        }

        public int size() {
            return queue.size();
        }
    }
}
