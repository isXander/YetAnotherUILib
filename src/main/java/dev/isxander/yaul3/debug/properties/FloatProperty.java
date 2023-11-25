package dev.isxander.yaul3.debug.properties;

import dev.isxander.yaul3.debug.Property;

public final class FloatProperty extends Property<Float> {
    public FloatProperty(String name, float productionDefault, float debugDefault) {
        super(name, productionDefault, debugDefault);
    }

    @Override
    public Float parseValue(String rawValue) {
        return Float.parseFloat(rawValue);
    }
}
