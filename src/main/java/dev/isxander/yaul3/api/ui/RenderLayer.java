package dev.isxander.yaul3.api.ui;

import dev.isxander.yaul3.impl.ui.render.ScissorRenderLayer;
import dev.isxander.yaul3.impl.ui.render.SolidBackgroundRenderLayer;
import dev.isxander.yaul3.impl.ui.render.StrokeRenderLayer;
import net.minecraft.client.gui.GuiGraphics;

public interface RenderLayer {
    void renderPre(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime);

    void renderPost(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime);

    static RenderLayer innerStroke(int width, int color) {
        return new StrokeRenderLayer(width, color, StrokeRenderLayer.StrokeType.INNER);
    }

    static RenderLayer outerStroke(int width, int color) {
        return new StrokeRenderLayer(width, color, StrokeRenderLayer.StrokeType.OUTER);
    }

    static RenderLayer centerStroke(int width, int color) {
        return new StrokeRenderLayer(width, color, StrokeRenderLayer.StrokeType.CENTER);
    }

    static RenderLayer scissor() {
        return new ScissorRenderLayer();
    }

    static RenderLayer solidBackground(int color) {
        return new SolidBackgroundRenderLayer(color);
    }
}
