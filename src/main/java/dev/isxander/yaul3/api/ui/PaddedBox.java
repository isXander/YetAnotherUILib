package dev.isxander.yaul3.api.ui;

import dev.isxander.yaul3.impl.ui.PaddedBoxImpl;

public interface PaddedBox extends ParentLayoutWidget {
    static PaddedBox create(LayoutWidget widget) {
        return new PaddedBoxImpl(widget);
    }

    static PaddedBox create() {
        return new PaddedBoxImpl();
    }

    PaddedBox setPadding(IntState padding);
    PaddedBox setPadding(IntState top, IntState right, IntState bottom, IntState left);
    PaddedBox setPaddingTop(IntState padding);
    PaddedBox setPaddingRight(IntState padding);
    PaddedBox setPaddingBottom(IntState padding);
    PaddedBox setPaddingLeft(IntState padding);
    PaddedBox setPadding(IntState topBottom, IntState leftRight);
    IntState getPaddingTop();
    IntState getPaddingRight();
    IntState getPaddingBottom();
    IntState getPaddingLeft();

    PaddedBox fitChild();

    @Override
    PaddedBox addWidgets(LayoutWidget... widgets);

    @Override
    PaddedBox setX(IntState x);

    @Override
    PaddedBox setY(IntState y);

    @Override
    PaddedBox setWidth(IntState width);

    @Override
    PaddedBox setHeight(IntState height);

    @Override
    PaddedBox appendRenderLayers(RenderLayer... layers);

    @Override
    PaddedBox insertRenderLayers(int index, RenderLayer... layers);
}
