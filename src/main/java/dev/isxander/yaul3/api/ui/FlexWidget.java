package dev.isxander.yaul3.api.ui;

import dev.isxander.yaul3.impl.ui.FlexWidgetImpl;

public interface FlexWidget extends ParentLayoutWidget {
    static FlexWidget create() {
        return new FlexWidgetImpl();
    }

    FlexWidget setDirection(FlexDirection direction);
    FlexDirection getDirection();

    FlexWidget setJustification(FlexJustification justification);
    FlexJustification getJustification();

    FlexWidget setAlign(FlexAlign align);
    FlexAlign getAlign();

    FlexWidget setGap(IntState gap);
    IntState getGap();

    @Override
    FlexWidget addWidgets(LayoutWidget... widgets);

    @Override
    FlexWidget setX(IntState x);

    @Override
    FlexWidget setY(IntState y);

    @Override
    FlexWidget setWidth(IntState width);

    @Override
    FlexWidget setHeight(IntState height);

    @Override
    FlexWidget appendRenderLayers(RenderLayer... layers);

    @Override
    FlexWidget insertRenderLayers(int index, RenderLayer... layers);
}
