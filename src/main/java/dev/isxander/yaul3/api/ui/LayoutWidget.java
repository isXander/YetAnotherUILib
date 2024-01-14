package dev.isxander.yaul3.api.ui;

import dev.isxander.yaul3.impl.ui.AbstractWidgetLayoutImpl;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface LayoutWidget extends StateOwner {
    <T extends GuiEventListener & NarratableEntry> void addToScreen(Consumer<T> widgetConsumer, Consumer<Renderable> renderableConsumer);
    void removeFromScreen(Consumer<GuiEventListener> widgetConsumer);

    LayoutWidget setX(IntState x);
    LayoutWidget setY(IntState y);

    LayoutWidget setWidth(IntState width);
    LayoutWidget setHeight(IntState height);


    IntState getX();
    IntState getY();

    IntState getWidth();
    IntState getHeight();

    LayoutWidget appendRenderLayers(RenderLayer... layers);
    LayoutWidget insertRenderLayers(int index, RenderLayer... layers);

    void setup();

    default boolean propagateDown() {
        return false;
    }

    @Nullable LayoutWidget getParent();
    void setParent(@Nullable LayoutWidget owner);


    static LayoutWidget of(AbstractWidget widget) {
        return new AbstractWidgetLayoutImpl(widget);
    }

    static LayoutWidget of(Component component, Font font) {
        return new AbstractWidgetLayoutImpl(new StringWidget(component, font));
    }
}
