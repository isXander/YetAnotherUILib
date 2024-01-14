package dev.isxander.yaul3.impl.ui.render;

import dev.isxander.yaul3.api.ui.RenderLayer;
import dev.isxander.yaul3.api.ui.LayoutWidget;
import net.minecraft.client.gui.GuiGraphics;

public class ScissorRenderLayer implements RenderLayer {
    @Override
    public void renderPre(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime) {
        graphics.enableScissor(widget.getX().get(), widget.getY().get(), widget.getX().get() + widget.getWidth().get(), widget.getY().get() + widget.getHeight().get());
    }

    @Override
    public void renderPost(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime) {
        graphics.disableScissor();
    }
}
