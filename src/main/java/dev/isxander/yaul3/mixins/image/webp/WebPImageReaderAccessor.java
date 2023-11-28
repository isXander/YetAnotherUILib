package dev.isxander.yaul3.mixins.image.webp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(targets = "com.twelvemonkeys.imageio.plugins.webp.WebPImageReader", remap = false)
public interface WebPImageReaderAccessor {
    @Accessor("frames")
    List<?> getFrames();
}
