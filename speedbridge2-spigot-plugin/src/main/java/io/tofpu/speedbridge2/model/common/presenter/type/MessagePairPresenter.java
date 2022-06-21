package io.tofpu.speedbridge2.model.common.presenter.type;

import io.tofpu.speedbridge2.model.common.presenter.MessagePresenterBase;

import java.util.LinkedHashMap;
import java.util.Map;

public class MessagePairPresenter extends MessagePresenterBase {
    private static final String TITLE_FORMAT = MessageTreePresenter.TREE_STYLE + " %s";
    private static final String PAIR_FORMAT =
            MessageTreePresenter.TREE_STYLE + "- %s: <white>%s";
    private static final String PAIR_FORMAT_WITH_NO_TITLE = PAIR_FORMAT.replace("--", "-");

    private final Map<String, String> pairMap = new LinkedHashMap<>();
    private final String title;
    private String key;

    public MessagePairPresenter(final String title, final Map<String, String> pairMap) {
        this.title = title;
        this.pairMap.putAll(pairMap);
    }

    @Override
    public MessagePresenterBase append(final String message) {
        if (key == null) {
            key = message;
        } else {
            this.pairMap.put(key, message);
            key = null;
        }
        return this;
    }

    @Override
    public String getResult() {
        final StringBuilder builder = new StringBuilder();
        final boolean hasTitle = title != null && !title.isEmpty();
        if (hasTitle) {
            builder.append("\n").append(String.format(TITLE_FORMAT, title));
        }

        final String pairFormat = hasTitle ? PAIR_FORMAT : PAIR_FORMAT_WITH_NO_TITLE;
        for (final Map.Entry<String, String> entry : pairMap.entrySet()) {
            builder.append("\n")
                    .append(String.format(pairFormat, entry.getKey(), entry.getValue()));
        }
        return builder.toString();
    }

    public static final class Builder {
        private final Map<String, String> pairMap = new LinkedHashMap<>();
        private String title;

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder pair(final String key, final String value) {
            pairMap.put(key, value);
            return this;
        }

        public MessagePresenterBase build() {
            return new MessagePairPresenter(title, pairMap);
        }
    }
}
