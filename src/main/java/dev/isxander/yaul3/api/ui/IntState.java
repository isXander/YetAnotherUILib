package dev.isxander.yaul3.api.ui;

import dev.isxander.yaul3.impl.ui.state.IntStateImpl;

public interface IntState extends State<Integer> {
    static IntState of(int value) {
        return new IntStateImpl(value);
    }

    IntState plus(IntState other);

    IntState minus(IntState other);

    IntState mul(IntState other);

    IntState div(IntState other);
}
