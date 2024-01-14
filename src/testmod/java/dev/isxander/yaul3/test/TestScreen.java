package dev.isxander.yaul3.test;


import dev.isxander.yaul3.api.animation.Animation;
import dev.isxander.yaul3.api.ui.*;
import dev.isxander.yaul3.impl.ui.AbstractLayoutWidget;
import dev.isxander.yaul3.impl.widgets.DynamicGridWidget;
import dev.isxander.yaul3.impl.widgets.ImageButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TestScreen extends Screen implements UnitHelper {

    public TestScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        DynamicGridWidget grid = new DynamicGridWidget(0, 0, this.width - 20, this.height - 20);

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

        //grid.visitWidgets(this::addRenderableWidget);

//        var layout = FlexWidget.create()
//                .setWidth(percentW(100)).setHeight(percentH(100))
//                .setJustification(FlexJustification.CENTER)
//                .setAlign(FlexAlign.CENTER)
//                .addWidgets(
//                        LayoutWidget.of(Component.literal("Screen Title"), font)
//                );
//        while (layout.justify()) {}
//        layout.addToScreen(this::addWidget, this::addRenderableOnly);

        IntState width = px(200);
        var animation = Animation.of(200)
                .consumerI(width::set, 200, 50);

        var layout = PaddedBox.create(
                FlexWidget.create()
                        .setDirection(FlexDirection.COLUMN)
                        .setJustification(FlexJustification.SPACE_BETWEEN)
                        .setAlign(FlexAlign.CENTER)
                        .addWidgets(
                                LayoutWidget.of(Component.literal("Screen Title"), font)
                                        .appendRenderLayers(RenderLayer.solidBackground(0xFF00FF00)),
                                FlexWidget.create()
                                        .setDirection(FlexDirection.COLUMN)
                                        //.setJustification(FlexJustification.CENTER)
                                        .setAlign(FlexAlign.STRETCH)
                                        .setGap(px(5))
                                        .setWidth(width)
                                        .appendRenderLayers(RenderLayer.solidBackground(0xFF0000FF))
                                        .addWidgets(
                                                LayoutWidget.of(Button.builder(Component.literal("Test 1"), btn -> animation.play()).build())
                                                        .appendRenderLayers(RenderLayer.solidBackground(0xFFFF0000)),
                                                LayoutWidget.of(Button.builder(Component.literal("Test 2"), btn -> {}).build())
                                                        .appendRenderLayers(RenderLayer.solidBackground(0xFFFF0000)),
                                                LayoutWidget.of(Button.builder(Component.literal("Test 3"), btn -> {}).build())
                                                        .appendRenderLayers(RenderLayer.solidBackground(0xFFFF0000)),
                                                LayoutWidget.of(Button.builder(Component.literal("Test 4"), btn -> {}).build())
                                                        .appendRenderLayers(RenderLayer.solidBackground(0xFFFF0000))
                                        ),
                                PaddedBox.create(
                                        FlexWidget.create()
                                                .setJustification(FlexJustification.CENTER)
                                                .setAlign(FlexAlign.CENTER)
                                                .setGap(rem(1))
                                                .appendRenderLayers(RenderLayer.solidBackground(0xFFFFFF47))
                                                .addWidgets(
                                                        LayoutWidget.of(Button.builder(Component.literal("Test 1"), btn -> {}).build())
                                                                .appendRenderLayers(RenderLayer.solidBackground(0xFF00FF00)),
                                                        LayoutWidget.of(Button.builder(Component.literal("Test 2"), btn -> {}).build())
                                                                .appendRenderLayers(RenderLayer.solidBackground(0xFF00FF00))
                                                )
                                )
                                        .fitChild()
                                        .setPadding(rem(2), rem(4))
                                        .appendRenderLayers(RenderLayer.solidBackground(0xFF4747FF))
                        )
                        .appendRenderLayers(RenderLayer.solidBackground(0xFFFF4747))
        )
                .setWidth(percentW(100)).setHeight(percentH(100))
                .setPadding(rem(2), zero())
                .appendRenderLayers(RenderLayer.solidBackground(0xFF000000));

        layout.setup();
        layout.addToScreen(this::addWidget, this::addRenderableOnly);

    }

    @Override
    public void resize(Minecraft client, int width, int height) {
        super.resize(client, width, height);
        this.rebuildWidgets();
    }
}
