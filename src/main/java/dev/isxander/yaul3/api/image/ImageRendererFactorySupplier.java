package dev.isxander.yaul3.api.image;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public interface ImageRendererFactorySupplier<T extends ImageRenderer> {
    RendererSupplier<T> createFactory(ResourceLocation location, Resource resource) throws Exception;

    interface RendererSupplier<T extends ImageRenderer> {
        T newRenderer();
    }
}
