package dev.isxander.yaul3.impl.image;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import dev.isxander.yaul3.debug.DebugProperties;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class AnimatedDynamicTextureImage extends DynamicTextureImage {
    private int currentFrame;
    private double lastFrameTime;

    private final double[] frameDelays;
    private final int frameCount;

    private final int packCols, packRows;

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    private final int frameWidth, frameHeight;

    private boolean paused = false;

    public AnimatedDynamicTextureImage(NativeImage image, int frameWidth, int frameHeight, int frameCount, double[] frameDelayMS, int packCols, int packRows, ResourceLocation uniqueLocation) {
        super(image, uniqueLocation);
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameCount = frameCount;
        this.frameDelays = frameDelayMS;
        this.packCols = packCols;
        this.packRows = packRows;
    }

    /**
     * Pause the animation.
     * @param isPaused Whether to pause the animation.
     */
    public void setPaused(boolean isPaused) {
        this.paused = isPaused;
    }

    /**
     * Move the animation to the next frame.
     */
    public void nextFrame() {
        this.currentFrame++;
        if (this.currentFrame >= this.frameCount - 1)
            this.currentFrame = 0;
    }

    /**
     * Move the animation to the previous frame.
     */
    public void previousFrame() {
        this.currentFrame--;
        if (this.currentFrame < 0)
            this.currentFrame = this.frameCount - 1;
    }

    /**
     * Reset the animation to the first frame.
     */
    public void reset() {
        this.currentFrame = 0;
    }

    @Override
    public int render(GuiGraphics graphics, int x, int y, int renderWidth, float tickDelta) {
        if (image == null) return 0;

        float ratio = renderWidth / (float)frameWidth;
        int targetHeight = (int) (frameHeight * ratio);

        int currentCol = currentFrame % packCols;
        int currentRow = (int) Math.floor(currentFrame / (double)packCols);

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(ratio, ratio, 1);

        if (DebugProperties.IMAGE_FILTERING.getValue()) {
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MAG_FILTER, GlConst.GL_LINEAR);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MIN_FILTER, GlConst.GL_LINEAR);
        }

        graphics.blit(
                uniqueLocation,
                0, 0,
                frameWidth * currentCol, frameHeight * currentRow,
                frameWidth, frameHeight,
                this.width, this.height
        );
        graphics.pose().popPose();

        if (frameCount > 1 && !paused) {
            double timeMS = Blaze3D.getTime() * 1000;
            if (lastFrameTime == 0) lastFrameTime = timeMS;
            if (timeMS - lastFrameTime >= frameDelays[currentFrame]) {
                nextFrame();
                lastFrameTime = timeMS;
            }
        }

        return targetHeight;
    }
}
