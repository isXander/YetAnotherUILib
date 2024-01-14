package dev.isxander.yaul3.impl.ui;

import dev.isxander.yaul3.api.ui.IntState;
import dev.isxander.yaul3.api.ui.RenderLayer;
import dev.isxander.yaul3.api.ui.LayoutWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractLayoutWidget implements LayoutWidget {
    private IntState x = IntState.of(0), y = IntState.of(0), width = IntState.of(0), height = IntState.of(0);
    private final List<RenderLayer> renderLayers = new ArrayList<>();
    private @Nullable LayoutWidget parent;
    public boolean justifying = false;

    @Override
    public final <T extends GuiEventListener & NarratableEntry> void addToScreen(Consumer<T> widgetConsumer, Consumer<Renderable> renderableConsumer) {
        addRenderLayersPre(renderableConsumer);
        innerAddToScreen(widgetConsumer, renderableConsumer);
        addRenderLayersPost(renderableConsumer);
    }

    protected <T extends GuiEventListener & NarratableEntry> void innerAddToScreen(Consumer<T> widgetConsumer, Consumer<Renderable> renderableConsumer) {

    }

    protected void addRenderLayersPre(Consumer<Renderable> renderableConsumer) {
        for (int i = 0; i < renderLayers.size(); i++) {
            RenderLayer layer = renderLayers.get(i);
            int z = i;
            renderableConsumer.accept((gui, mouseX, mouseY, partialTick) ->
                    layer.renderPre(this, gui, z, mouseX, mouseY, partialTick));
        }
    }

    protected void addRenderLayersPost(Consumer<Renderable> renderableConsumer) {
        for (int i = 0; i < renderLayers.size(); i++) {
            RenderLayer layer = renderLayers.get(i);
            int z = i;
            renderableConsumer.accept((gui, mouseX, mouseY, partialTick) ->
                    layer.renderPost(this, gui, z, mouseX, mouseY, partialTick));
        }
    }

    @Override
    public void removeFromScreen(Consumer<GuiEventListener> widgetConsumer) {

    }

    @Override
    public void setup() {
        Validate.isTrue(parent == null, "Only a root widget can be setup!");

        int iterations = 0;
        justifying = true;
        while (propagateDown() && iterations++ <= 100) {}
        justifying = false;
        if (iterations >= 100) {
            throw new IllegalStateException("LayoutWidget#setup() took too long!");
        }
    }

    @Override
    public void onStateUpdate() {
        if (parent != null) {
            parent.onStateUpdate();
        } else if (!justifying) {
            // reached the top of the tree
            justifying = true;
            while (propagateDown()) {}
            justifying = false;
        }
    }

    @Override
    public LayoutWidget setX(IntState x) {
        this.x = x;
        this.x.setOwner(this);
        return this;
    }

    @Override
    public IntState getX() {
        return x;
    }

    @Override
    public LayoutWidget setY(IntState y) {
        this.y = y;
        this.y.setOwner(this);
        return this;
    }

    @Override
    public IntState getY() {
        return y;
    }

    @Override
    public LayoutWidget setWidth(IntState width) {
        this.width = width;
        this.width.setOwner(this);
        return this;
    }

    @Override
    public IntState getWidth() {
        return width;
    }

    @Override
    public LayoutWidget setHeight(IntState height) {
        this.height = height;
        this.height.setOwner(this);
        return this;
    }

    @Override
    public IntState getHeight() {
        return height;
    }

    @Override
    public LayoutWidget appendRenderLayers(RenderLayer... layers) {
        renderLayers.addAll(List.of(layers));
        return this;
    }

    @Override
    public LayoutWidget insertRenderLayers(int index, RenderLayer... layers) {
        renderLayers.addAll(index, List.of(layers));
        return this;
    }

    @Override
    public void setParent(@Nullable LayoutWidget owner) {
        this.parent = owner;
    }

    @Nullable
    @Override
    public LayoutWidget getParent() {
        return parent;
    }
}
