package dev.isxander.yaul3.impl.ui.state;

import dev.isxander.yaul3.api.ui.State;
import dev.isxander.yaul3.api.ui.StateOwner;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class StateImpl<T> implements State<T> {
    private T value;
    private StateOwner owner;

    public StateImpl(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean set(T value, boolean notify) {
        boolean changed = !Objects.equals(this.value, value);
        this.value = value;

        if (notify && changed && owner != null) {
            owner.onStateUpdate();
        }

        return changed;
    }

    @Override
    public boolean set(T value) {
        return set(value, true);
    }

    @Override
    public StateOwner getOwner() {
        return owner;
    }

    @Override
    public void setOwner(StateOwner owner) {
        Validate.isTrue(this.owner == null, "State already has an owner!");

        this.owner = owner;
    }
}
