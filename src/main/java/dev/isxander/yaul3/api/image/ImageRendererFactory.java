package dev.isxander.yaul3.api.image;

/**
 * A factory for creating {@link ImageRenderer}s.
 */
public interface ImageRendererFactory<T extends ImageRenderer> {
    /**
     * Prepares the image. This can be run off-thread,
     * and should NOT contain any GL calls whatsoever.
     */
    ImageSupplier<T> prepareImage() throws Exception;

    /**
     * Whether the factory should prepare images off-thread or on thread.
     * It's recommended to prepare images off-thread.
     */
    default boolean requiresOffThreadPreparation() {
        return true;
    }

    /**
     * A supplier for an image.
     */
    interface ImageSupplier<T extends ImageRenderer> {
        T completeImage() throws Exception;
    }

    /**
     * A factory that requires on-thread preparation.
     */
    interface OnThread<T extends ImageRenderer> extends ImageRendererFactory<T> {
        @Override
        default boolean requiresOffThreadPreparation() {
            return false;
        }
    }
}
