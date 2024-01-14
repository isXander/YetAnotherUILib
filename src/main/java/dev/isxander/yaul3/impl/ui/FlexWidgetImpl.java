package dev.isxander.yaul3.impl.ui;

import com.google.common.collect.Lists;
import dev.isxander.yaul3.api.ui.*;

import java.util.List;
import java.util.function.*;

public final class FlexWidgetImpl extends AbstractParentLayoutWidget implements FlexWidget {
    private IntState gap = IntState.of(0);
    private FlexDirection direction = FlexDirection.ROW;
    private FlexJustification justification = FlexJustification.START;
    private FlexAlign align = FlexAlign.START;
    private final Axis hAxis = new Axis(
            () -> this.getX().get(),
            () -> this.getWidth().get(),
            (x) -> this.getX().set(x),
            (width) -> this.getWidth().set(width),
            (widget) -> widget.getX().get(),
            (widget) -> widget.getWidth().get(),
            (widget, x) -> widget.getX().set(x),
            (widget, width) -> widget.getWidth().set(width)
    );
    private final Axis vAxis = new Axis(
            () -> this.getY().get(),
            () -> this.getHeight().get(),
            (y) -> this.getY().set(y),
            (height) -> this.getHeight().set(height),
            (widget) -> widget.getY().get(),
            (widget) -> widget.getHeight().get(),
            (widget, y) -> widget.getY().set(y),
            (widget, height) -> widget.getHeight().set(height)
    );

    @Override
    public boolean propagateDown() {
        justifying = true;
        boolean changed = super.propagateDown();

        // justify widgets horizontally
        Axis justifyAxis = isRow() ? hAxis : vAxis;
        changed |= switch (justification) {
            case START -> justifyStart(justifyAxis);
            case END -> justifyEnd(justifyAxis);
            case CENTER -> justifyCenter(justifyAxis);
            case SPACE_BETWEEN -> justifySpaceBetween(justifyAxis);
            case SPACE_AROUND -> justifySpaceAround(justifyAxis);
            case SPACE_EVENLY -> justifySpaceEvenly(justifyAxis);
        };

        // align widgets vertically
        Axis alignAxis = isRow() ? vAxis : hAxis;
        changed |= switch (align) {
            case START -> alignStart(alignAxis);
            case CENTER -> alignCenter(alignAxis);
            case END -> alignEnd(alignAxis);
            case STRETCH -> alignStretch(alignAxis);
        };


        if (changed) {
            int minX = widgets.stream().mapToInt(widget -> widget.getX().get()).min().orElse(0);
            int minY = widgets.stream().mapToInt(widget -> widget.getY().get()).min().orElse(0);
            int maxX = widgets.stream().mapToInt(widget -> widget.getX().get() + widget.getWidth().get()).max().orElse(0);
            int maxY = widgets.stream().mapToInt(widget -> widget.getY().get() + widget.getHeight().get()).max().orElse(0);

            // ensure width and height contain all children
            int contentWidth = maxX - minX;
            int contentHeight = maxY - minY;

            if (contentWidth > getWidth().get()) {
                getWidth().set(contentWidth);
            }

            if (contentHeight > getHeight().get()) {
                getHeight().set(contentHeight);
            }
        }

        return changed;
    }

    /**
     * Children populate left to right
     */
    private boolean justifyStart(Axis axis) {
        boolean changed = false;

        int pos = axis.thisPos();
        for (LayoutWidget widget : widgetList()) {
            changed |= axis.childPos(widget) != pos;

            axis.childPos(widget, pos);
            pos += axis.childLength(widget) + gap.get();
        }

        return changed;
    }

    /**
     * Children populate right to left
     */
    private boolean justifyEnd(Axis axis) {
        boolean changed = false;

        int pos = axis.thisPos() + axis.thisLength();
        for (LayoutWidget widget : widgetList()) {
            pos -= axis.childLength(widget);
            changed |= axis.childPos(widget, pos);
            pos -= gap.get();
        }

        return changed;
    }

    /**
     * Children populate center to edges
     */
    private boolean justifyCenter(Axis axis) {
        boolean changed = false;

        int totalLength = widgetList().stream()
                .mapToInt(axis::childLength)
                .sum() + (gap.get() * (widgets.size() - 1));

        int centerPos = axis.thisPos() + (axis.thisLength() / 2);
        int pos = centerPos - (totalLength / 2);
        for (LayoutWidget widget : widgetList()) {
            changed |= axis.childPos(widget, pos);
            pos += axis.childLength(widget) + gap.get();
        }

        return changed;
    }

    /**
     * Fills the space between children with equal gaps to fill parent
     */
    private boolean justifySpaceBetween(Axis axis) {
        boolean changed = false;

        int totalWidgetLength = widgetList().stream()
                .mapToInt(axis::childLength)
                .sum();
        int requiredGap = (axis.thisLength() - totalWidgetLength) / (widgets.size() - 1);
        int pos = axis.thisPos();
        for (LayoutWidget widget : widgetList()) {
            changed |= axis.childPos(widget, pos);
            pos += axis.childLength(widget) + requiredGap;
        }

        return changed;
    }

    /**
     * Like space between, but also adds half a gap to the start and end
     */
    private boolean justifySpaceAround(Axis axis) {
        boolean changed = false;

        int totalWidgetLength = widgetList().stream()
                .mapToInt(axis::childLength)
                .sum();
        int totalGapLength = axis.thisLength() - totalWidgetLength;
        int fullGapLength = totalGapLength / widgets.size() - 1;
        int halfGapLength = fullGapLength / 2;

        int x = axis.thisPos() + halfGapLength;
        for (LayoutWidget widget : widgetList()) {
            changed |= axis.childPos(widget, x);
            x += axis.childLength(widget) + fullGapLength;
        }

        return changed;
    }

    /**
     * Space all widgets to fill with equal gaps, inside and outside
     */
    private boolean justifySpaceEvenly(Axis axis) {
        boolean changed = false;

        int totalWidgetLength = widgetList().stream()
                .mapToInt(axis::childLength)
                .sum();
        int totalGapLength = axis.thisLength() - totalWidgetLength;
        int fullGapLength = totalGapLength / (widgets.size() + 1);

        int x = axis.thisPos() + fullGapLength;
        for (LayoutWidget widget : widgetList()) {
            changed |= axis.childPos(widget, x);
            x += axis.childLength(widget) + fullGapLength;
        }

        return changed;
    }

    private boolean alignStart(Axis axis) {
        boolean changed = false;

        int y = axis.thisPos();
        for (LayoutWidget widget : widgets) {
            changed |= axis.childPos(widget, y);
        }

        return changed;
    }

    private boolean alignCenter(Axis axis) {
        boolean changed = false;
        int centerY = axis.thisPos() + (axis.thisLength() / 2);

        for (LayoutWidget widget : widgets) {
            int targetY = centerY - (axis.childLength(widget) / 2);
            changed |= axis.childPos(widget, targetY);
        }

        return changed;
    }

    private boolean alignEnd(Axis axis) {
        boolean changed = false;

        int y = axis.thisPos() + axis.thisLength();
        for (LayoutWidget widget : widgets) {
            int targetY = y - axis.childLength(widget);
            changed |= axis.childPos(widget, targetY);
        }

        return changed;
    }

    private boolean alignStretch(Axis axis) {
        boolean changed = false;

        for (LayoutWidget widget : widgets) {
            changed |= axis.childPos(widget, axis.thisPos());
            changed |= axis.childLength(widget, axis.thisLength());
        }

        return changed;
    }

    private List<LayoutWidget> widgetList() {
        return switch (direction) {
            case ROW, COLUMN -> widgets;
            case ROW_REVERSE, COLUMN_REVERSE -> Lists.reverse(widgets);
        };
    }

    private boolean isRow() {
        return direction == FlexDirection.ROW || direction == FlexDirection.ROW_REVERSE;
    }

    @Override
    public FlexWidget setGap(IntState gap) {
        this.gap = gap;
        return this;
    }

    @Override
    public IntState getGap() {
        return gap;
    }

    @Override
    public FlexWidget setDirection(FlexDirection direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public FlexDirection getDirection() {
        return direction;
    }

    @Override
    public FlexWidget setJustification(FlexJustification justification) {
        this.justification = justification;
        return this;
    }

    @Override
    public FlexJustification getJustification() {
        return justification;
    }

    @Override
    public FlexWidget setAlign(FlexAlign align) {
        this.align = align;
        return this;
    }

    @Override
    public FlexAlign getAlign() {
        return align;
    }

    @Override
    public FlexWidget addWidgets(LayoutWidget... widgets) {
        super.addWidgets(widgets);
        return this;
    }

    @Override
    public FlexWidget setX(IntState x) {
        super.setX(x);
        return this;
    }

    @Override
    public FlexWidget setY(IntState y) {
        super.setY(y);
        return this;
    }

    @Override
    public FlexWidget setWidth(IntState width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public FlexWidget setHeight(IntState height) {
        super.setHeight(height);
        return this;
    }

    @Override
    public FlexWidget appendRenderLayers(RenderLayer... layers) {
        super.appendRenderLayers(layers);
        return this;
    }

    @Override
    public FlexWidget insertRenderLayers(int index, RenderLayer... layers) {
        super.insertRenderLayers(index, layers);
        return this;
    }

    private record Axis(
            Supplier<Integer> thisPosGetter,
            Supplier<Integer> thisLengthGetter,
            Function<Integer, Boolean> thisPosSetter,
            Function<Integer, Boolean> thisLengthSetter,

            Function<LayoutWidget, Integer> childPosGetter,
            Function<LayoutWidget, Integer> childLengthGetter,
            BiFunction<LayoutWidget, Integer, Boolean> childPosSetter,
            BiFunction<LayoutWidget, Integer, Boolean> childLengthSetter
    ){
        public int thisPos() {
            return thisPosGetter.get();
        }

        public int thisLength() {
            return thisLengthGetter.get();
        }

        public boolean thisPos(int pos) {
            return thisPosSetter.apply(pos);
        }

        public boolean thisLength(int length) {
            return thisLengthSetter.apply(length);
        }

        public int childPos(LayoutWidget widget) {
            return childPosGetter.apply(widget);
        }

        public int childLength(LayoutWidget widget) {
            return childLengthGetter.apply(widget);
        }

        public boolean childPos(LayoutWidget widget, int pos) {
            return childPosSetter.apply(widget, pos);
        }

        public boolean childLength(LayoutWidget widget, int length) {
            return childLengthSetter.apply(widget, length);
        }
    }
}
