package io.tofpu.speedbridge2.command.help.collector;

import java.util.*;

public class TreeNode<T> {
    protected final Map<String, TreeNode<T>> children;
    protected T data;
    protected TreeNode<T> parent;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedHashMap<>();
    }

    public void data(T data) {
        this.data = data;
    }

    public TreeNode<T> path(String name, T data) {
        return path(name, new TreeNode<>(data));
    }

    TreeNode<T> path(String name, TreeNode<T> childNode) {
        return children.computeIfAbsent(name, t -> {
            childNode.parent = TreeNode.this;
            return childNode;
        });
    }

    public TreeNode<T> parent() {
        return parent;
    }

    public T data() {
        return data;
    }

    public Collection<TreeNode<T>> children() {
        return children.values();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TreeNode.class.getSimpleName() + "[", "]")
                .add("data=" + data)
                .add("children=" + children)
                .toString();
    }
}
