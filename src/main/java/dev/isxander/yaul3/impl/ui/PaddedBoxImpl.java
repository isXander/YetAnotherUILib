package dev.isxander.yaul3.impl.ui;

import dev.isxander.yaul3.api.ui.IntState;
import dev.isxander.yaul3.api.ui.LayoutWidget;
import dev.isxander.yaul3.api.ui.PaddedBox;
import dev.isxander.yaul3.api.ui.RenderLayer;
import org.apache.commons.lang3.Validate;

public class PaddedBoxImpl extends AbstractParentLayoutWidget implements PaddedBox {
    private final LayoutWidget widget;
    private IntState paddingLeft = IntState.of(0), paddingTop = IntState.of(0), paddingRight = IntState.of(0), paddingBottom = IntState.of(0);

    public PaddedBoxImpl(LayoutWidget widget) {
        super.addWidgets(this.widget = widget);
    }

    public PaddedBoxImpl() {
        this.widget = null;
    }

    @Override
    public boolean propagateDown() {
        if (widgets.isEmpty()) return false;

        Validate.isTrue(widgets.size() == 1, "PaddedBox can only have one child");

        boolean changed = super.propagateDown();

        changed |= widget.getX().set(getX().get() + paddingLeft.get());
        changed |= widget.getY().set(getY().get() + paddingTop.get());
        changed |= widget.getWidth().set(getWidth().get() - paddingLeft.get() - paddingRight.get());
        changed |= widget.getHeight().set(getHeight().get() - paddingTop.get() - paddingBottom.get());

        return changed;
    }

    @Override
    public PaddedBox fitChild() {
        if (widgets.isEmpty()) return this;

        this.getWidth().set(widget.getWidth().get());
        this.getHeight().set(widget.getHeight().get());
        return this;
    }

    @Override
    public PaddedBox setPadding(IntState padding) {
        return setPadding(padding, padding, padding, padding);
    }

    @Override
    public PaddedBox setPadding(IntState top, IntState right, IntState bottom, IntState left) {
        this.setPaddingTop(top);
        this.setPaddingRight(right);
        this.setPaddingBottom(bottom);
        this.setPaddingLeft(left);
        return this;
    }

    @Override
    public PaddedBox setPadding(IntState topBottom, IntState leftRight) {
        return setPadding(topBottom, leftRight, topBottom, leftRight);
    }

    @Override
    public PaddedBox setPaddingTop(IntState padding) {
        this.paddingTop = padding;
        return this;
    }

    @Override
    public PaddedBox setPaddingRight(IntState padding) {
        this.paddingRight = padding;
        return this;
    }

    @Override
    public PaddedBox setPaddingBottom(IntState padding) {
        this.paddingBottom = padding;
        return this;
    }

    @Override
    public PaddedBox setPaddingLeft(IntState padding) {
        this.paddingLeft = padding;
        return this;
    }

    @Override
    public IntState getPaddingTop() {
        return paddingTop;
    }

    @Override
    public IntState getPaddingRight() {
        return paddingRight;
    }

    @Override
    public IntState getPaddingBottom() {
        return paddingBottom;
    }

    @Override
    public IntState getPaddingLeft() {
        return paddingLeft;
    }

    @Override
    public PaddedBox addWidgets(LayoutWidget... widgets) {
        throw new UnsupportedOperationException("PaddedBox can only have one child");
    }

    @Override
    public PaddedBox setX(IntState x) {
        super.setX(x);
        return this;
    }

    @Override
    public PaddedBox setY(IntState y) {
        super.setY(y);
        return this;
    }

    @Override
    public PaddedBox setWidth(IntState width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public PaddedBox setHeight(IntState height) {
        super.setHeight(height);
        return this;
    }

    @Override
    public PaddedBox appendRenderLayers(RenderLayer... layers) {
        super.appendRenderLayers(layers);
        return this;
    }

    @Override
    public PaddedBox insertRenderLayers(int index, RenderLayer... layers) {
        super.insertRenderLayers(index, layers);
        return this;
    }
}
