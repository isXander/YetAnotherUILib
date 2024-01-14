package dev.isxander.yaul3.impl.ui.render;

import dev.isxander.yaul3.api.ui.RenderLayer;
import dev.isxander.yaul3.api.ui.LayoutWidget;
import net.minecraft.client.gui.GuiGraphics;

public class SolidBackgroundRenderLayer implements RenderLayer {
    private final int color;

    public SolidBackgroundRenderLayer(int color) {
        this.color = color;
    }

    @Override
    public void renderPre(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime) {
        graphics.fill(widget.getX().get(), widget.getY().get(), widget.getX().get() + widget.getWidth().get(), widget.getY().get() + widget.getHeight().get(), z, color);
    }

    @Override
    public void renderPost(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime) {

    }
}
