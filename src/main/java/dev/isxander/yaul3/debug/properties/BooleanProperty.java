package dev.isxander.yaul3.debug.properties;

import dev.isxander.yaul3.debug.Property;

public class BooleanProperty extends Property<Boolean> {
        public BooleanProperty(String name, boolean productionDefault, boolean debugDefault) {
            super(name, productionDefault, debugDefault);
        }

        @Override
        public Boolean parseValue(String rawValue) {
            return Boolean.parseBoolean(rawValue);
        }
}
