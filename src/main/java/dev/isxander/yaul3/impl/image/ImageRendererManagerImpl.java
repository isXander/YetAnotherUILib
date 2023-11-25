package dev.isxander.yaul3.impl.image;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yaul3.api.image.ImageRenderer;
import dev.isxander.yaul3.api.image.ImageRendererFactory;
import dev.isxander.yaul3.api.image.ImageRendererManager;
import dev.isxander.yaul3.util.CompletedSupplier;
import dev.isxander.yaul3.util.LoggerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class ImageRendererManagerImpl implements ImageRendererManager {
    private static final Logger LOGGER = LoggerManager.createLogger("ImageRendererManager");
    private static final ExecutorService SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadExecutor(task -> new Thread(task, "YACL Image Prep"));

    private final Map<ResourceLocation, CompletableFuture<ImageRenderer>> IMAGE_CACHE = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ImageRenderer> CompletableFuture<T> registerImage(ResourceLocation id, ImageRendererFactory<T> factory) {
        if (IMAGE_CACHE.containsKey(id)) {
            return (CompletableFuture<T>) IMAGE_CACHE.get(id);
        }

        var future = new CompletableFuture<ImageRenderer>();
        IMAGE_CACHE.put(id, future);

        SINGLE_THREAD_EXECUTOR.submit(() -> {
            Supplier<Optional<ImageRendererFactory.ImageSupplier<T>>> supplier =
                    factory.requiresOffThreadPreparation()
                            ? CompletedSupplier.of(safelyPrepareFactory(id, factory))
                            : () -> safelyPrepareFactory(id, factory);

            Minecraft.getInstance().execute(() -> completeImageFactory(id, supplier, future));
        });

        return (CompletableFuture<T>) future;
    }

    @Override
    public <T extends ImageRenderer> void completeImageFactory(ResourceLocation id, Supplier<Optional<ImageRendererFactory.ImageSupplier<T>>> supplier, CompletableFuture<ImageRenderer> future) {
        RenderSystem.assertOnRenderThread();

        ImageRendererFactory.ImageSupplier<T> completableImage = supplier.get().orElse(null);
        if (completableImage == null) {
            return;
        }

        if (future.isDone()) {
            LOGGER.error("Image '{}' was already completed", id);
            return;
        }

        ImageRenderer image;
        try {
            image = completableImage.completeImage();
        } catch (Exception e) {
            LOGGER.error("Failed to create image '{}'", id, e);
            return;
        }

        future.complete(image);
    }

    @Override
    public void closeAll() {
        SINGLE_THREAD_EXECUTOR.shutdownNow();
        IMAGE_CACHE.values().removeIf(future -> {
            if (future.isDone()) {
                future.join().close();
            }
            return true;
        });
    }

    private <T extends ImageRenderer> Optional<ImageRendererFactory.ImageSupplier<T>> safelyPrepareFactory(ResourceLocation id, ImageRendererFactory<T> factory) {
        try {
            return Optional.of(factory.prepareImage());
        } catch (Exception e) {
            LOGGER.error("Failed to prepare image '{}'", id, e);
            IMAGE_CACHE.remove(id);
            return Optional.empty();
        }
    }
}
