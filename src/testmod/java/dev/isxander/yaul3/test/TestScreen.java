package dev.isxander.yaul3.test;


import dev.isxander.yaul3.impl.widgets.DynamicGridWidget;
import dev.isxander.yaul3.impl.widgets.ImageButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TestScreen extends Screen {

    public TestScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        DynamicGridWidget grid = new DynamicGridWidget(10, 10, this.width - 20, this.height - 20);

        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")), 4, 1);
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")), 2, 2);
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));
        grid.addChild(new ImageButtonWidget(0, 0, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/home.webp")));

        grid.setPadding(5);

        grid.calculateLayout();

        grid.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void resize(Minecraft client, int width, int height) {
        super.resize(client, width, height);
        this.rebuildWidgets();
    }
}
