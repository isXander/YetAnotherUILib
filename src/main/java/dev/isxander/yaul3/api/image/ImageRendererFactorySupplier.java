package dev.isxander.yaul3.api.image;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.Closeable;
import java.util.function.Consumer;

public interface ImageRendererFactorySupplier<T extends ImageRenderer> {
    RendererSupplier<T> createFactory(ResourceLocation location, Resource resource, Consumer<AutoCloseable> closeableConsumer) throws Exception;

    interface RendererSupplier<T extends ImageRenderer> {
        T newRenderer();
    }
}
