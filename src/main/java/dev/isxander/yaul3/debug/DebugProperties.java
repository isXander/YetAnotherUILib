package dev.isxander.yaul3.debug;

import dev.isxander.yaul3.debug.properties.BooleanProperty;

public class DebugProperties {
    /**
     * Applies GL filtering to rendering images.
     */
    public static final Property<Boolean> IMAGE_FILTERING = new BooleanProperty("imageFiltering", false, false);
}
