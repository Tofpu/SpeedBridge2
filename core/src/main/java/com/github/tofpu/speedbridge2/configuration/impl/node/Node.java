package com.github.tofpu.speedbridge2.configuration.impl.node;

import com.github.tofpu.speedbridge2.configuration.Configuration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class Node {

    private final Configuration delegate;
    private final String[] paths;

    public Node(Configuration delegate, String[] paths) {
        this.delegate = delegate;
        this.paths = paths;
    }

    public NodeConfiguration path(final String key) {
        String[] newPath = new String[this.paths.length + 1];

        System.arraycopy(paths, 0, newPath, 0, paths.length);
        newPath[this.paths.length] = key;

        System.out.println("old path: " + Arrays.toString(paths));
        System.out.println("new path: " + Arrays.toString(newPath));

        return new NodeConfiguration(delegate, newPath);
    }

    public NodeConfiguration previous() {
        String[] newPath = new String[this.paths.length - 1];

        System.arraycopy(paths, 0, newPath, 0, paths.length - 1);

        System.out.println("old path: " + Arrays.toString(paths));
        System.out.println("new path: " + Arrays.toString(newPath));

        return new NodeConfiguration(delegate, newPath);
    }

    public void set(String key, Object obj) {
        Result result = getResult();
        System.out.println("before: nestMap: " + result.nestMap());

        result.nestMap().put(key, obj);
        System.out.println("after: nestMap: " + result.nestMap());

        System.out.println("rootMap: " + result.rootMap());
        delegate.set(paths[0], result.rootMap());
    }

    public Object get(String key, Object defaultValue) {
        Result result = getResult();
        return result.nestMap().getOrDefault(key, defaultValue);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private Result getResult() {
        final Map<String, Object> rootMap = (Map<String, Object>) delegate.data()
            .getOrDefault(paths[0], new LinkedHashMap<>());

        // department
        // --list
        // ----new york

//        String previousPath = paths[0];
        Map<String, Object> previousMap = rootMap;
        Map<String, Object> nestMap = rootMap;
        for (int i = 1; i < paths.length; i++) {
            String path = paths[i];

            nestMap = (Map<String, Object>) nestMap.getOrDefault(path, new LinkedHashMap<>());
            previousMap.put(path, nestMap);
            previousMap = nestMap;
        }
        return new Result(rootMap, nestMap);
    }

    private static class Result {

        private final Map<String, Object> rootMap;
        private final Map<String, Object> nestMap;

        public Result(Map<String, Object> rootMap, Map<String, Object> nestMap) {
            this.rootMap = rootMap;
            this.nestMap = nestMap;
        }

        public Map<String, Object> rootMap() {
            return rootMap;
        }

        public Map<String, Object> nestMap() {
            return nestMap;
        }
    }
}
