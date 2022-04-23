package io.tofpu.speedbridge2.model.common.presenter;

import java.util.function.Supplier;

public interface MessagePresenterHolder {
    MessagePresenterHolderImpl append(final Supplier<? extends MessagePresenterBase> presenter);
    String getResult();
}
