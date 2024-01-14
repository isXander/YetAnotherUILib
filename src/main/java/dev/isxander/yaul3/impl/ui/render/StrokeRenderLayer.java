package dev.isxander.yaul3.impl.ui.render;

import dev.isxander.yaul3.api.ui.RenderLayer;
import dev.isxander.yaul3.api.ui.LayoutWidget;
import net.minecraft.client.gui.GuiGraphics;

public class StrokeRenderLayer implements RenderLayer {
    private final int width;
    private final int color;
    private final StrokeType strokeType;

    public StrokeRenderLayer(int width, int color, StrokeType strokeType) {
        this.width = width;
        this.color = color;
        this.strokeType = strokeType;
    }

    @Override
    public void renderPre(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime) {

    }

    @Override
    public void renderPost(LayoutWidget widget, GuiGraphics graphics, int z, int mouseX, int mouseY, float deltaTime) {
        int off1 = switch (strokeType) {
            case INNER -> 0;
            case OUTER -> -width;
            case CENTER -> -width / 2;
        };
        int off2 = switch (strokeType) {
            case INNER -> 0;
            case OUTER -> width;
            case CENTER -> width / 2;
        };

        int x = widget.getX().get();
        int y = widget.getY().get();
        int width = widget.getWidth().get();
        int height = widget.getHeight().get();

        // top line
        graphics.fill(x + off1, y + off1, x + width + off2, y + off1 + this.width, z, color);
        // bottom line
        graphics.fill(x + off1, y + height + off2 - this.width, x + width + off2, y + height + off2, z, color);
        // left line
        graphics.fill(x + off1, y + off1 + this.width, x + off1 + this.width, y + height + off2 - this.width, z, color);
        // right line
        graphics.fill(x + width + off2 - this.width, y + off1 + this.width, x + width + off2, y + height + off2 - this.width, z, color);
    }

    public enum StrokeType {
        INNER,
        OUTER,
        CENTER
    }
}
