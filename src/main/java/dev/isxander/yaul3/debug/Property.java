package dev.isxander.yaul3.debug;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public abstract class Property<T> {
    private final String name;
    private final T productionDefault;
    private final T debugDefault;

    public Property(String name, T productionDefault, T debugDefault) {
        this.name = name;
        this.productionDefault = productionDefault;
        this.debugDefault = debugDefault;
    }

    public abstract T parseValue(String rawValue);

    public T getValue() {
        String rawValue = getRawValue();

        if (rawValue == null) {
            return FabricLoader.getInstance().isDevelopmentEnvironment() ? debugDefault : productionDefault;
        } else {
            return parseValue(rawValue);
        }
    }

    private @Nullable String getRawValue() {
        return System.getProperty("yaul.debug." + name);
    }

}
