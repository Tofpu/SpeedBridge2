package io.tofpu.speedbridge2.model.common.presenter;

public abstract class MessagePresenterBase {
    public abstract MessagePresenterBase append(final String message);

    public abstract String getResult();

    public abstract static class Builder {
        public abstract Builder append(final String message);
        public abstract MessagePresenterBase build();
    }
}
