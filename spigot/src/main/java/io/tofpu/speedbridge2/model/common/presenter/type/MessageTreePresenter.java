package io.tofpu.speedbridge2.model.common.presenter.type;

import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageTreePresenter extends MessagePresenterBase {
    public static final String TREE_STYLE = "<dark_gray>| -";

    private static final String TITLE_FORMAT = TREE_STYLE + " <reset>%s";

    private static final String MESSAGE_FORMAT = TITLE_FORMAT.replace("-", "--");
    private static final String MESSAGE_FORMAT_WITH_NO_TITLE = TITLE_FORMAT;


    private final String title;
    private final List<String> treeList;

    public MessageTreePresenter() {
        this("", Collections.emptyList());
    }

    public MessageTreePresenter(String title, final List<String> messages) {
        this.title = title;
        this.treeList = new ArrayList<>();

        this.treeList.addAll(messages);
    }

    @Override
    public MessagePresenterBase append(final String text) {
        treeList.add(text);
        return this;
    }

    @Override
    public String getResult() {
        final StringBuilder builder = new StringBuilder();

        final boolean hasTitle = !title.isEmpty();
        if (hasTitle) {
            builder.append("\n").append(String.format(TITLE_FORMAT, title));
        }

        final String messageFormat = hasTitle ? MESSAGE_FORMAT : MESSAGE_FORMAT_WITH_NO_TITLE;
        for (final String message : treeList) {
            builder.append("\n").append(String.format(messageFormat, message));
        }
        return builder.toString();
    }

    public static class Builder {
        private final List<String> messages = new ArrayList<>();
        private String title;

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder message(final String message) {
            messages.add(message);
            return this;
        }

        public MessagePresenterBase build() {
            return new MessageTreePresenter(title, messages);
        }
    }
}
