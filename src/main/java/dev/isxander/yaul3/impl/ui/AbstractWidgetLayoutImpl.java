package dev.isxander.yaul3.impl.ui;

import dev.isxander.yaul3.api.ui.*;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class AbstractWidgetLayoutImpl implements LayoutWidget {
    private final AbstractWidget widget;
    private final List<RenderLayer> renderLayers = new ArrayList<>();

    private IntState x, y, width, height;
    private LayoutWidget parent;

    public AbstractWidgetLayoutImpl(AbstractWidget widget) {
        this.widget = widget;
        this.x = IntState.of(widget.getX());
        this.x.setOwner(createStateOwner(x, widget::setX));
        this.y = IntState.of(widget.getY());
        this.y.setOwner(createStateOwner(y, widget::setY));
        this.width = IntState.of(widget.getWidth());
        this.width.setOwner(createStateOwner(width, widget::setWidth));
        this.height = IntState.of(widget.getHeight());
        this.height.setOwner(createStateOwner(height, widget::setHeight));

    }

    @Override
    public LayoutWidget setX(IntState x) {
        this.x = x;
        this.x.setOwner(createStateOwner(x, widget::setX));
        return this;
    }

    @Override
    public LayoutWidget setY(IntState y) {
        this.y = y;
        this.y.setOwner(createStateOwner(y, widget::setY));
        return this;
    }

    @Override
    public LayoutWidget setWidth(IntState width) {
        this.width = width;
        this.width.setOwner(createStateOwner(width, widget::setWidth));
        return this;
    }

    @Override
    public LayoutWidget setHeight(IntState height) {
        this.height = height;
        this.height.setOwner(createStateOwner(height, widget::setHeight));
        return this;
    }

    @Override
    public IntState getX() {
        return this.x;
    }

    @Override
    public IntState getY() {
        return y;
    }

    @Override
    public IntState getWidth() {
        return width;
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
    public void setup() {

    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> void addToScreen(Consumer<T> widgetConsumer, Consumer<Renderable> renderableConsumer) {
        for (int i = 0; i < renderLayers.size(); i++) {
            RenderLayer layer = renderLayers.get(i);
            int z = i;
            renderableConsumer.accept((gui, mouseX, mouseY, partialTick) ->
                    layer.renderPre(this, gui, z, mouseX, mouseY, partialTick));
        }

        widgetConsumer.accept((T) widget);
        renderableConsumer.accept(widget);

        for (int i = 0; i < renderLayers.size(); i++) {
            RenderLayer layer = renderLayers.get(i);
            int z = i;
            renderableConsumer.accept((gui, mouseX, mouseY, partialTick) ->
                    layer.renderPost(this, gui, z, mouseX, mouseY, partialTick));
        }
    }

    @Override
    public void removeFromScreen(Consumer<GuiEventListener> widgetConsumer) {
        widgetConsumer.accept(widget);
    }

    @Override
    public void setParent(LayoutWidget parent) {
        this.parent = parent;
    }

    @Nullable
    @Override
    public LayoutWidget getParent() {
        return parent;
    }

    public AbstractWidget widget() {
        return widget;
    }

    private StateOwner createStateOwner(IntState state, Consumer<Integer> setter) {
        setter.accept(state.get());
        return () -> {
            setter.accept(state.get());
            onStateUpdate();
        };
    }

    @Override
    public void onStateUpdate() {
        if (parent != null) {
            parent.onStateUpdate();
        }
    }
}
