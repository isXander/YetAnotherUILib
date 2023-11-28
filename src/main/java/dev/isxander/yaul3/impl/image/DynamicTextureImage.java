package dev.isxander.yaul3.impl.image;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yaul3.api.image.ImageRenderer;
import dev.isxander.yaul3.debug.DebugProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class DynamicTextureImage implements ImageRenderer {
    protected static final TextureManager textureManager = Minecraft.getInstance().getTextureManager();

    protected NativeImage image;
    protected AbstractTexture texture;
    protected final ResourceLocation uniqueLocation;
    protected final int width, height;

    public DynamicTextureImage(NativeImage image, ResourceLocation location) {
        RenderSystem.assertOnRenderThread();

        this.image = image;
        this.uniqueLocation = location;
        this.texture = textureManager.getTexture(this.uniqueLocation, null);
        if (this.texture == null) {
            this.texture = new DynamicTexture(this.image);
            textureManager.register(this.uniqueLocation, this.texture);
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public int render(GuiGraphics graphics, int x, int y, int renderWidth, float tickDelta) {
        if (image == null) return 0;

        float ratio = renderWidth / (float)this.width;
        int targetHeight = (int) (this.height * ratio);

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(ratio, ratio, 1);

        if (DebugProperties.IMAGE_FILTERING.getValue()) {
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MAG_FILTER, GlConst.GL_LINEAR);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MIN_FILTER, GlConst.GL_LINEAR);
        }

        graphics.blit(uniqueLocation, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        graphics.pose().popPose();

        return targetHeight;
    }

    @Override
    public void close() {
        image.close();
        image = null;
        texture = null;
        textureManager.release(uniqueLocation);
    }
}
