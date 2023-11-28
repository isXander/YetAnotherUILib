package dev.isxander.yaul3.impl.image;

import dev.isxander.yaul3.api.image.*;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public class ImageRendererManagerImpl implements ImageRendererManager {
    private final Map<ResourceLocation, ImageRendererFactorySupplier.RendererSupplier<?>> IMAGE_RENDERER_FACTORIES = new HashMap<>();
    private final Map<Predicate<ResourceLocation>, ImageRendererFactorySupplier<?>> FACTORIES = new HashMap<>();

    private final List<AutoCloseable> closeables = new ArrayList<>();

    private boolean locked;

    public ImageRendererManagerImpl() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ReloadListener());

        registerImagePreloader(location -> location.getPath().endsWith(".webp"), ImageFactories.createWEBPFactorySupplier());
        registerImagePreloader(location -> location.getPath().endsWith(".gif"), ImageFactories.createGIFFactorySupplier());
    }

    @Override
    public <T extends ImageRenderer> T createPreloadedImage(ResourceLocation location) {
        var rendererFactory = Optional.ofNullable(IMAGE_RENDERER_FACTORIES.get(location))
                .map(it -> (ImageRendererFactorySupplier.RendererSupplier<T>) it);

        return rendererFactory.orElseThrow(() -> new IllegalArgumentException(location + " is not a loaded image renderer.")).newRenderer();
    }

    @Override
    public <T extends ImageRenderer> void registerImagePreloader(Predicate<ResourceLocation> location, ImageRendererFactorySupplier<T> factory) {
        Validate.isTrue(!locked, "Cannot register image factory after resource reload has begun.");
        FACTORIES.put(location, factory);
    }


    private class ReloadListener implements IdentifiableResourceReloadListener {
        @Override
        public ResourceLocation getFabricId() {
            return new ResourceLocation("yet_another_ui_lib_v1", "image_preloader");
        }

        @Override
        public @NotNull CompletableFuture<Void> reload(
                PreparationBarrier synchronizer,
                ResourceManager manager,
                ProfilerFiller prepareProfiler,
                ProfilerFiller applyProfiler,
                Executor prepareExecutor,
                Executor applyExecutor
        ) {
            locked = true;
            IMAGE_RENDERER_FACTORIES.clear();

            Map<ResourceLocation, Resource> imageResources = manager.listResources(
                    "textures",
                    location -> FACTORIES.keySet().stream().anyMatch(predicate -> predicate.test(location))
            );

            List<CompletableFuture<Void>> futures = new ArrayList<>(imageResources.size());

            for (Map.Entry<ResourceLocation, Resource> entry : imageResources.entrySet()) {
                ResourceLocation location = entry.getKey();
                Resource resource = entry.getValue();

                ImageRendererFactorySupplier<?> factory = FACTORIES
                        .entrySet()
                        .stream()
                        .filter(factoryEntry -> factoryEntry.getKey().test(location))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(null);

                if (factory == null) {
                    throw new IllegalStateException("Could not find valid image factory provider for resource: " + location);
                }

                CompletableFuture<ImageRendererFactorySupplier.RendererSupplier<?>> imageFuture =
                        CompletableFuture.supplyAsync(
                                () -> wrapFactoryCreationError(
                                        location,
                                        () -> factory.createFactory(location, resource, closeables::add)
                                ), prepareExecutor
                        )
                        .thenCompose(synchronizer::wait)
                        .thenApplyAsync(rendererSupplier -> IMAGE_RENDERER_FACTORIES.put(location, rendererSupplier), applyExecutor);

                imageFuture.whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        CrashReport crashReport = CrashReport.forThrowable(throwable, "Failed to load image");
                        CrashReportCategory category = crashReport.addCategory("YACL Gui");
                        category.setDetail("Image identifier", location.toString());
                        throw new ReportedException(crashReport);
                    }
                });
            }

            return CompletableFuture.allOf();
        }

        private <T> T wrapFactoryCreationError(ResourceLocation location, DangerousSupplier<T> supplier) {
            try {
                return supplier.get();
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Failed to load image");
                CrashReportCategory category = crashReport.addCategory("YACL Gui");
                category.setDetail("Image identifier", location.toString());
                throw new ReportedException(crashReport);
            }
        }
    }

    @FunctionalInterface
    interface DangerousSupplier<T> {
        T get() throws Exception;
    }
}
