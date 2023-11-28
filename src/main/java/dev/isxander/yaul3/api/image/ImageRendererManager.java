package dev.isxander.yaul3.api.image;

import dev.isxander.yaul3.YAUL3;
import dev.isxander.yaul3.api.image.ImageRenderer;
import dev.isxander.yaul3.api.image.ImageRendererFactory;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface ImageRendererManager {
    /**
     * Register an image to be rendered.
     * @param id The ID of the image.
     * @param factory The factory used to create the image.
     * @return A future that will be completed when the image is ready to be rendered.
     * @param <T> The type of the image.
     */
    <T extends ImageRenderer> CompletableFuture<T> registerImage(ResourceLocation id, ImageRendererFactory<T> factory);

    /**
     * Load the image factory on the render thread and complete the future associated with the image.
     * @param id The ID of the image.
     * @param supplier The supplier of the image factory.
     * @param future The future to complete.
     * @param <T> The type of the image.
     */
    <T extends ImageRenderer> void completeImageFactory(ResourceLocation id, Supplier<Optional<ImageRendererFactory.ImageSupplier<T>>> supplier, CompletableFuture<ImageRenderer> future);

    /**
     * Close all images and force any pending images to be completed immediately on the main thread.
     */
    void closeAll();

    /**
     * Get an instance of the image renderer manager.
     * @return The image renderer manager.
     */
    static ImageRendererManager getInstance() {
        return YAUL3.getInstance().imageRendererManager;
    }
}
