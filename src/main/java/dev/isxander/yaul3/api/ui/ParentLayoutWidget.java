package dev.isxander.yaul3.api.ui;

import java.util.Collection;

public interface ParentLayoutWidget extends LayoutWidget {
    ParentLayoutWidget addWidgets(LayoutWidget... widgets);

    Collection<LayoutWidget> getWidgets();

    @Override
    ParentLayoutWidget setX(IntState x);

    @Override
    ParentLayoutWidget setY(IntState y);

    @Override
    ParentLayoutWidget setWidth(IntState width);

    @Override
    ParentLayoutWidget setHeight(IntState height);

    @Override
    ParentLayoutWidget appendRenderLayers(RenderLayer... layers);

    @Override
    ParentLayoutWidget insertRenderLayers(int index, RenderLayer... layers);

}
