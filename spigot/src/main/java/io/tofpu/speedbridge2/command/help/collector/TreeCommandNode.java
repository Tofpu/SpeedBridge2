package io.tofpu.speedbridge2.command.help.collector;

import java.util.Iterator;

public class TreeCommandNode extends TreeNode<CommandInfo> {
    public TreeCommandNode(CommandInfo data) {
        super(data);
    }

    @Override
    public TreeCommandNode path(String name, CommandInfo data) {
        return (TreeCommandNode) path(name, new TreeCommandNode(data));
    }

    public Iterator<TreeCommandNode> childrenIterator() {
        return new Iterator<TreeCommandNode>() {
            final Iterator<TreeNode<CommandInfo>> iterator = TreeCommandNode.super.children().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public TreeCommandNode next() {
                return (TreeCommandNode) iterator.next();
            }
        };
    }
}
