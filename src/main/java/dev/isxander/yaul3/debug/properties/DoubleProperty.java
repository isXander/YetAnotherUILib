package dev.isxander.yaul3.debug.properties;

import dev.isxander.yaul3.debug.Property;

public final class DoubleProperty extends Property<Double> {
    public DoubleProperty(String name, double productionDefault, double debugDefault) {
        super(name, productionDefault, debugDefault);
    }

    @Override
    public Double parseValue(String rawValue) {
        return Double.parseDouble(rawValue);
    }
}
