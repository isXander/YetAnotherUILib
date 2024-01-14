package dev.isxander.yaul3.impl.ui;

import com.google.common.collect.ImmutableList;
import dev.isxander.yaul3.api.ui.*;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractParentLayoutWidget extends AbstractLayoutWidget implements ParentLayoutWidget {
    protected final List<LayoutWidget> widgets = new ArrayList<>();

    @Override
    public ParentLayoutWidget addWidgets(LayoutWidget... widgets) {
        List<LayoutWidget> list = List.of(widgets);
        list.forEach(widget -> widget.setParent(this));
        this.widgets.addAll(list);
        return this;
    }

    @Override
    public Collection<LayoutWidget> getWidgets() {
        return ImmutableList.copyOf(widgets);
    }

    @Override
    protected <T extends GuiEventListener & NarratableEntry> void innerAddToScreen(Consumer<T> widgetConsumer, Consumer<Renderable> renderableConsumer) {
        widgets.forEach(widget -> widget.addToScreen(widgetConsumer, renderableConsumer));
    }

    @Override
    public void removeFromScreen(Consumer<GuiEventListener> widgetConsumer) {
        super.removeFromScreen(widgetConsumer);
        widgets.forEach(widget -> widget.removeFromScreen(widgetConsumer));
    }

    @Override
    public boolean propagateDown() {
        boolean changed = false;

        for (LayoutWidget widget : widgets) {
            changed |= widget.propagateDown();
        }

        return changed;
    }

    @Override
    public ParentLayoutWidget setX(IntState x) {
        super.setX(x);
        return this;
    }

    @Override
    public ParentLayoutWidget setY(IntState y) {
        super.setY(y);
        return this;
    }

    @Override
    public ParentLayoutWidget setWidth(IntState width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public ParentLayoutWidget setHeight(IntState height) {
        super.setHeight(height);
        return this;
    }

    @Override
    public ParentLayoutWidget appendRenderLayers(RenderLayer... layers) {
        super.appendRenderLayers(layers);
        return this;
    }

    @Override
    public ParentLayoutWidget insertRenderLayers(int index, RenderLayer... layers) {
        super.insertRenderLayers(index, layers);
        return this;
    }
}
