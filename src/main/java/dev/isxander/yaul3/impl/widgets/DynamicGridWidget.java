package dev.isxander.yaul3.impl.widgets;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.AbstractLayout;
import net.minecraft.client.gui.layouts.LayoutElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DynamicGridWidget extends AbstractLayout {
    private final List<GridItem> children = new ArrayList<>();

    public DynamicGridWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void addChild(AbstractWidget widget, int cellHeight, int cellWidth) {
        this.children.add(new GridItem(cellHeight, cellWidth, widget));
    }

    public void addChild(AbstractWidget widget) {
        this.children.add(new GridItem(-1, -1, widget));
    }

    private boolean canFit(int gridX, int gridY, int cellWidth, int cellHeight, int optimalCells, boolean[][] grid) {
        if (gridX >= optimalCells || gridY >= optimalCells) {
            return false;
        }

        for (int x = gridX; x < gridX + cellWidth; x++) {
            for (int y = gridY; y < gridY + cellHeight; y++) {
                if (x >= grid.length || y >= grid[x].length) {
                    throw new RuntimeException("Impossible to fit widget in grid!");
                }

                if (grid[x][y]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Recalculates the layout of this widget.
     */
    public void calculateLayout() {
        int totalCells = 0;
        for (GridItem child : this.children) {
            int widgetCells = child.getCellWidth() * child.getCellHeight();
            totalCells += widgetCells;
        }

        int optimalCells = (int) Math.ceil(Math.sqrt(totalCells));

        int cellWidth = this.width / optimalCells;
        int cellHeight = this.height / optimalCells;

        boolean[][] grid = new boolean[optimalCells][optimalCells];

        int currentX = getX();
        int currentY = getY();

        for (GridItem child : this.children) {
            int gridX = currentX / cellWidth;
            int gridY = currentY / cellHeight;

            while (!canFit(gridX, gridY, Math.abs(child.getCellWidth()) , Math.abs(child.getCellHeight()), optimalCells, grid)) {
                currentX += cellWidth;
                if (currentX >= this.width) {
                    currentX = getX();
                    currentY += cellHeight;
                }

                gridX = currentX / cellWidth;
                gridY = currentY / cellHeight;
            }

            if (gridX > optimalCells || gridY > optimalCells) {
                throw new RuntimeException("Impossible to fit widget in grid!");
            }

            if(grid[gridX][gridY]) {
                currentX += cellWidth;
                if (currentX >= this.width) {
                    currentX = getX();
                    currentY += cellHeight;
                }

                gridX = currentX / cellWidth;
                gridY = currentY / cellHeight;
            }

            int thisCellWidth = cellWidth;
            int thisCellHeight = cellHeight;

            boolean isMultiCell = child.getCellHeight() > 1 || child.getCellWidth() > 1;
            if (child.getCellWidth() != -1) {
                thisCellWidth = child.getCellWidth() * cellWidth;
            }

            if (child.getCellHeight() != -1) {
                thisCellHeight = child.getCellHeight() * cellHeight;
            }

            if(isMultiCell) {
                // Mark all cells this widget uses as taken.
                int minX = gridX;
                int minY = gridY;
                int maxX = gridX + child.getCellWidth();
                int maxY = gridY + child.getCellHeight();

                for(int x = minX; x < maxX; x++) {
                    for(int y = minY; y < maxY; y++) {
                        grid[x][y] = true;
                    }
                }

                System.out.println(grid);
            } else {
                grid[gridX][gridY] = true;
            }

            child.getWidget().setX(currentX);
            child.getWidget().setY(currentY);
            child.getWidget().setWidth(thisCellWidth);
            child.getWidget().setHeight(thisCellHeight);

            currentX += thisCellWidth;
            if (currentX >= this.width) {
                currentX = getX();
                currentY += thisCellHeight;
            }
        }

    }

    @Override
    public void visitChildren(Consumer<LayoutElement> widgetConsumer) {
        this.children.stream().map(GridItem::getWidget).forEach(widgetConsumer);
    }

    public static class GridItem {
        // -1 = don't care about size.
        private final int cellHeight;
        // -1 = don't care about size
        private final int cellWidth;
        private final AbstractWidget widget;

        public GridItem(int cellHeight, int cellWidth, AbstractWidget widget) {
            this.cellHeight = cellHeight;
            this.cellWidth = cellWidth;
            this.widget = widget;
        }

        public int getCellHeight() {
            return cellHeight;
        }

        public int getCellWidth() {
            return cellWidth;
        }

        public AbstractWidget getWidget() {
            return widget;
        }
    }
}
