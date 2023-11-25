package dev.isxander.yaul3.util;

import java.util.function.Supplier;

/**
 * A supplier where the return value has been pre-evaluated.
 * @param <T>
 */
public class CompletedSupplier<T> implements Supplier<T> {
    /**
     * The value to return.
     */
    private final T value;

    /**
     * Creates a new completed supplier.
     * @param value The pre-evaluated value.
     */

    public CompletedSupplier(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    /**
     * Creates a new completed supplier.
     * @param value The pre-evaluated value.
     * @return The completed supplier.
     * @param <T> The type of the value.
     */
    public static <T> CompletedSupplier<T> of(T value) {
        return new CompletedSupplier<>(value);
    }
}
