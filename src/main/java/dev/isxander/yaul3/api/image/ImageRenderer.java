package dev.isxander.yaul3.api.image;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Represents a renderable image.
 */
public interface ImageRenderer {
    /**
     * Render the image at the given coordinates.
     * @param x The x coordinate to render at.
     * @param y The y coordinate to render at.
     * @param renderWidth The width of the image.
     * @param tickDelta The current tick delta.
     */
    int render(GuiGraphics graphics, int x, int y, int renderWidth, float tickDelta);

    default void tick() {}

    /**
     * Dispose of any resources used by the image.
     */
    void close();
}
