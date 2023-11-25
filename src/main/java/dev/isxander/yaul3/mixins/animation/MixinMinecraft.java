package dev.isxander.yaul3.mixins.animation;

import dev.isxander.yaul3.impl.animation.Animator;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    public abstract float getDeltaFrameTime();

    @Inject(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GameRenderer;render(FJZ)V"
            )
    )
    private void tickAnimator(boolean tick, CallbackInfo ci) {
        Animator.INSTANCE.tick(this.getDeltaFrameTime());
    }
}
