package dev.isxander.yaul3.mixins.webp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.awt.*;

@Mixin(targets = "com.twelvemonkeys.imageio.plugins.webp.AnimationFrame", remap = false)
public interface AnimationFrameAccessor {
    @Accessor("duration")
    int getDuration();

    @Accessor("bounds")
    Rectangle getBounds();
}
