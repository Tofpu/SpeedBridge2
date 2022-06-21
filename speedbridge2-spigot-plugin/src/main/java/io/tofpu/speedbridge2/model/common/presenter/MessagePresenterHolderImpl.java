package io.tofpu.speedbridge2.model.common.presenter;

import java.util.function.Supplier;

public final class MessagePresenterHolderImpl implements MessagePresenterHolder {
    private static final String HEADER_FORMAT = "<dark_gray>| <<gold>-</gold>> " +
                                                "<yellow><bold>%s </bold><reset>";

    private final StringBuilder builder;

    public MessagePresenterHolderImpl(String header) {
        header = String.format(HEADER_FORMAT, header);

        this.builder = new StringBuilder(header);
    }

    @Override
    public MessagePresenterHolderImpl append(final Supplier<? extends MessagePresenterBase> presenter) {
        builder.append("\n").append(presenter.get().getResult()).append("<reset>");
        return this;
    }

    @Override
    public String getResult() {
        return builder.toString();
    }
}
