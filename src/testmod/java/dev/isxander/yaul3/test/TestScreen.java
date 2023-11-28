package dev.isxander.yaul3.test;


import dev.isxander.yaul3.impl.widgets.ImageButtonWidget;
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

        this.addWidget(new ImageButtonWidget(5, 5, 200, 100, Component.literal("Home"), new ResourceLocation("yaul_test", "textures/home.webp")));
        this.addWidget(new ImageButtonWidget(210, 5, 50, 50, Component.literal("Test"), new ResourceLocation("yaul_test", "textures/large.webp")));
    }
}
