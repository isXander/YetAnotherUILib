package dev.isxander.yaul3.test.mixins;

import dev.isxander.yaul3.test.TestScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void initWidgets(CallbackInfo ci) {
        this.addWidget(Button.builder(Component.literal("test"), (btn) -> this.minecraft.setScreen(new TestScreen(Component.literal("Test")))).bounds(5, 5, 50, 20).build());
    }
}
