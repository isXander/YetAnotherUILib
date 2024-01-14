package dev.isxander.yaul3.api.ui;

import dev.isxander.yaul3.impl.ui.state.StateImpl;

public interface State<T> {
    static <T> State<T> of(T value) {
        return new StateImpl<>(value);
    }

    T get();

    boolean set(T value);
    boolean set(T value, boolean notify);

    StateOwner getOwner();
    void setOwner(StateOwner owner);
}
