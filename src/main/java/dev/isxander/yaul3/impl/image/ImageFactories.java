package dev.isxander.yaul3.impl.image;

import com.mojang.blaze3d.platform.NativeImage;
import com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi;
import dev.isxander.yaul3.api.image.ImageRendererFactorySupplier;
import dev.isxander.yaul3.mixins.image.webp.AnimationFrameAccessor;
import dev.isxander.yaul3.mixins.image.webp.WebPImageReaderAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public final class ImageFactories {
    public static ImageRendererFactorySupplier<AnimatedDynamicTextureImage> createWEBPFactorySupplier() {
        return (location, resource, closeableConsumer) -> {
            try (InputStream is = resource.open()) {
                ImageReader reader = new WebPImageReaderSpi().createReaderInstance();
                reader.setInput(ImageIO.createImageInputStream(is));

                int numImages = reader.getNumImages(true); // Force reading of all frames
                AnimFrameProvider animFrameFunction = i -> null;
                if (numImages > 1) {
                    List<?> frames = ((WebPImageReaderAccessor) reader).getFrames();
                    animFrameFunction = i -> {
                        AnimationFrameAccessor frame = (AnimationFrameAccessor) frames.get(i);
                        Rectangle bounds = frame.getBounds();
                        return new AnimFrame(frame.getDuration(), bounds.x, bounds.y);
                    };
                }

                return stitchAndFinishImageReader(reader, animFrameFunction, location, closeableConsumer);
            }
        };
    }

    public static ImageRendererFactorySupplier<AnimatedDynamicTextureImage> createGIFFactorySupplier() {
        return (location, resource, closeableConsumer) -> {
            try (InputStream is = resource.open()) {
                ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
                reader.setInput(ImageIO.createImageInputStream(is));

                AnimFrameProvider animFrameFunction = i -> {
                    IIOMetadata metadata = reader.getImageMetadata(i);
                    String metaFormatName = metadata.getNativeMetadataFormatName();
                    IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);
                    IIOMetadataNode graphicsControlExtensionNode = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
                    int delay = Integer.parseInt(graphicsControlExtensionNode.getAttribute("delayTime")) * 10;

                    return new AnimFrame(delay, 0, 0);
                };

                return stitchAndFinishImageReader(reader, animFrameFunction, location, closeableConsumer);
            }
        };
    }

    private static ImageRendererFactorySupplier.RendererSupplier<AnimatedDynamicTextureImage> stitchAndFinishImageReader(ImageReader reader, AnimFrameProvider animFrameProvider, ResourceLocation location, Consumer<AutoCloseable> closeables) throws Exception {
        if (reader.isSeekForwardOnly()) {
            throw new RuntimeException("Image reader is not seekable");
        }

        int frameCount = reader.getNumImages(true);

        // Because this is being backed into a texture atlas, we need a maximum dimension
        // so you can get the texture atlas size.
        // Smaller frames are given black borders
        int frameWidth = IntStream.range(0, frameCount).map(i -> {
            try {
                return reader.getWidth(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).max().orElseThrow();
        int frameHeight = IntStream.range(0, frameCount).map(i -> {
            try {
                return reader.getHeight(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).max().orElseThrow();

        // Packs the frames into an optimal 1:1 texture.
        // OpenGL can only have texture axis with a max of 32768 pixels,
        // and packing them to that length is not efficient, apparently.
        double ratio = frameWidth / (double)frameHeight;
        int cols = (int)Math.ceil(Math.sqrt(frameCount) / Math.sqrt(ratio));
        int rows = (int)Math.ceil(frameCount / (double)cols);

        NativeImage image = new NativeImage(NativeImage.Format.RGBA, frameWidth * cols, frameHeight * rows, false);

//            // Fill whole atlas with black, as each frame may have different dimensions
//            // that would cause borders of transparent pixels to appear around the frames
//            for (int x = 0; x < frameWidth * cols; x++) {
//                for (int y = 0; y < frameHeight * rows; y++) {
//                    image.setPixelRGBA(x, y, 0xFF000000);
//                }
//            }

        BufferedImage bi = null;
        Graphics2D graphics = null;

        // each frame may have a different delay
        double[] frameDelays = new double[frameCount];

        for (int i = 0; i < frameCount; i++) {
            AnimFrame frame = animFrameProvider.get(i);
            if (frameCount > 1) // frame will be null if not animation
                frameDelays[i] = frame.durationMS;

            if (bi == null) {
                // first frame...
                bi = reader.read(i);
                graphics = bi.createGraphics();
            } else {
                // WebP reader sometimes provides delta frames, (only the pixels that changed since the last frame)
                // so instead of overwriting the image every frame, we draw delta frames on top of the previous frame
                // to keep a complete image.
                BufferedImage deltaFrame = reader.read(i);
                graphics.drawImage(deltaFrame, frame.xOffset, frame.yOffset, null);
            }

            // Each frame may have different dimensions, so we need to center them.
            int xOffset = (frameWidth - bi.getWidth()) / 2;
            int yOffset = (frameHeight - bi.getHeight()) / 2;

            for (int w = 0; w < bi.getWidth(); w++) {
                for (int h = 0; h < bi.getHeight(); h++) {
                    int rgb = bi.getRGB(w, h);
                    int r = FastColor.ARGB32.red(rgb);
                    int g = FastColor.ARGB32.green(rgb);
                    int b = FastColor.ARGB32.blue(rgb);
                    int a = FastColor.ARGB32.alpha(rgb);

                    int col = i % cols;
                    int row = (int) Math.floor(i / (double)cols);

                    image.setPixelRGBA(
                            frameWidth * col + w + xOffset,
                            frameHeight * row + h + yOffset,
                            FastColor.ABGR32.color(a, b, g, r) // NativeImage uses ABGR for some reason
                    );
                }
            }
        }

        if (graphics != null)
            graphics.dispose();
        reader.dispose();

        closeables.accept(image);

        return () -> new AnimatedDynamicTextureImage(image, frameWidth, frameHeight, frameCount, frameDelays, cols, rows, suffixLocation(location, "atlas"));
    }

    @FunctionalInterface
    private interface AnimFrameProvider {
        AnimFrame get(int frame) throws Exception;
    }

    private record AnimFrame(int durationMS, int xOffset, int yOffset) {}

    private static ResourceLocation suffixLocation(ResourceLocation location, String suffix) {
        return new ResourceLocation(location.getNamespace(), location.getPath() + "-" + suffix);
    }
}
