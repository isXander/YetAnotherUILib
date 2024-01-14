package dev.isxander.yaul3.impl.ui.state;

import dev.isxander.yaul3.api.ui.IntState;

public class IntStateImpl extends StateImpl<Integer> implements IntState {
    public IntStateImpl(Integer value) {
        super(value);
    }

    @Override
    public IntState plus(IntState other) {
        IntState newState = new IntStateImpl(get() + other.get());
        newState.setOwner(getOwner());
        return newState;
    }

    @Override
    public IntState minus(IntState other) {
        IntState newState = new IntStateImpl(get() - other.get());
        newState.setOwner(getOwner());
        return newState;
    }

    @Override
    public IntState mul(IntState other) {
        IntState newState = new IntStateImpl(get() * other.get());
        newState.setOwner(getOwner());
        return newState;
    }

    @Override
    public IntState div(IntState other) {
        IntState newState = new IntStateImpl(get() / other.get());
        newState.setOwner(getOwner());
        return newState;
    }
}
