package dev.isxander.yaul3.impl.widgets;


import dev.isxander.yaul3.api.image.ImageRendererManager;
import dev.isxander.yaul3.impl.image.AnimatedDynamicTextureImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.lwjgl.opengl.GL20.*;

public class ImageButtonWidget extends AbstractWidget {
    private AnimatedDynamicTextureImage image;
    private int contentHeight = this.height;

    public ImageButtonWidget(int x, int y, int width, int height, Component message, ResourceLocation image) {
        super(x, y, width, height, message);
        this.image = ImageRendererManager.getInstance().createPreloadedImage(image);
    }

    float durationHovered = 1f;

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.enableScissor(getX(), getY(), getX() + width, getY() + height);
        this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

        if(this.isHovered) {
            durationHovered += delta / 2f;
        } else {
            if(durationHovered < 0) {
                durationHovered = 0;
            } else {
                durationHovered -= durationHovered / 4f;
            }
        }

        // Ease in out lerp.
        float alphaScale = Mth.clampedLerp(0.9f, 0.2f, Mth.clamp(durationHovered - 1f, 0.0f, 1.0f));

        // Scale the image so that the image height is the same as the button height.
        float neededWidth = image.getFrameWidth() * ((float) this.height / image.getFrameHeight());

        // Scale the image to fit within the width and height of the button.
        graphics.pose().pushPose();
        // gl bilinear scaling.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        this.contentHeight = image.render(graphics, getX(), getY(), (int) neededWidth, delta);
        graphics.pose().popPose();

//        context.drawTexture(image, getX(), getY(), this.width, this.height, 0, 0, 1920, 1080, 1920, 1080);

        int greyColor = FastColor.ARGB32.color((int) (alphaScale * 255), 0, 0, 0);
        graphics.fill(getX(), getY(), getX() + width, getY() + height, greyColor);

        // Draw text.
        var client = Minecraft.getInstance();

        float fontScaling = 1.24f;

        int unscaledTextX = this.getX() + 5;
        int unscaledTextY = this.getY() + this.height - client.font.lineHeight - 5;
        int textX = (int) (unscaledTextX / fontScaling);
        int textY = (int) (unscaledTextY / fontScaling);
        int endX = (int) ((this.getX() + this.width - 5) / fontScaling);
        int endY = (int) ((this.getY() + this.height - 5) / fontScaling);

        graphics.fill(unscaledTextX - 5, unscaledTextY - 5, unscaledTextX + this.width - 5, unscaledTextY + client.font.lineHeight + 5, 0xAF000000);

        graphics.pose().pushPose();
        graphics.pose().scale(fontScaling, fontScaling, 1.0f);

//            context.fill(textX, textY, endX, endY, 0xFFFF2F00);
        renderScrollingString(graphics, client.font, getMessage(), textX, textX, textY, endX, endY, 0xFFFFFF);

        graphics.pose().popPose();

        // Draw border.
        graphics.renderOutline(getX(), getY(), width, height, 0x0FFFFFFF);
        graphics.disableScissor();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {}
}
