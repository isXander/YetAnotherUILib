package dev.isxander.yaul3.debug.properties;

import dev.isxander.yaul3.debug.Property;

public final class IntegerProperty extends Property<Integer> {
    public IntegerProperty(String name, int productionDefault, int debugDefault) {
        super(name, productionDefault, debugDefault);
    }

    @Override
    public Integer parseValue(String rawValue) {
        return Integer.parseInt(rawValue);
    }
}
