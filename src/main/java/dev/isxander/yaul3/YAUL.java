package dev.isxander.yaul3;

import dev.isxander.yaul3.api.image.ImageRendererManager;
import dev.isxander.yaul3.impl.image.ImageRendererManagerImpl;
import net.fabricmc.api.ClientModInitializer;

public class YAUL implements ClientModInitializer {
    private static YAUL instance = null;
    public ImageRendererManager imageRendererManager;

    public static YAUL getInstance() {
        if(instance == null) {
            throw new RuntimeException("YAUL3 is not initialized yet!");
        }

        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;

        imageRendererManager = new ImageRendererManagerImpl();
    }
}
