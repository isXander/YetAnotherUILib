package dev.isxander.yaul3.debug.properties;

import dev.isxander.yaul3.debug.Property;

public final class StringProperty extends Property<String> {
    public StringProperty(String name, String productionDefault, String debugDefault) {
        super(name, productionDefault, debugDefault);
    }

    @Override
    public String parseValue(String rawValue) {
        return rawValue;
    }
}
