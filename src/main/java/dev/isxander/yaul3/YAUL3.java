package dev.isxander.yaul3;

import dev.isxander.yaul3.api.image.ImageRendererManager;
import dev.isxander.yaul3.impl.image.ImageRendererManagerImpl;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

public class YAUL3 implements ClientModInitializer {
    private static YAUL3 instance = null;
    public ImageRendererManager imageRendererManager;

    public static YAUL3 getInstance() {
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
