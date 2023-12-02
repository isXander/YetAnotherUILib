package dev.isxander.yaul3.api.image;

import dev.isxander.yaul3.YAUL;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Predicate;

public interface ImageRendererManager {
    static ImageRendererManager getInstance() {
        return YAUL.getInstance().imageRendererManager;
    }

    <T extends ImageRenderer> T createPreloadedImage(ResourceLocation location);

    <T extends ImageRenderer> void registerImagePreloader(Predicate<ResourceLocation> location, ImageRendererFactorySupplier<T> factory);
}
