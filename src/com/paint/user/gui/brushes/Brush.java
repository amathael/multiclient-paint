package com.paint.user.gui.brushes;

import com.paint.user.gui.FieldPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static com.paint.global.utils.Utils.*;

/**
 * Created by Admin on 06.02.2017.
 */
public class Brush {

    int brushSize, drawingState = 3;
    boolean rubberMode, fillMode, markersMode;
    private Color color = Color.black;

    volatile LinkedList<Point> targets;
    FieldPanel field;

    Brush(FieldPanel field) {
        brushSize = 1;
        rubberMode = false;
        fillMode = false;
        markersMode = true;
        targets = new LinkedList<>();
        this.field = field;
    }

    Brush(FieldPanel field, int brushSize) {
        this.brushSize = brushSize;
        rubberMode = false;
        fillMode = false;
        markersMode = true;
        targets = new LinkedList<>();
        this.field = field;
    }

    Color getDrawingColor() {
        return rubberMode ? FieldPanel.BACKGROUND_COLOR : color;
    }

    Color getMouseColor() {
        return transparentColor(getDrawingColor());
    }

    public boolean inputActive() {
        return targets.size() > 0;
    }

    void addTarget(Point point) {
        targets.add(point);
        field.repaint();
    }

    void confirmPainting() {
        paintTargets(field.getField().getGraphics());
        field.repaintAll();
        targets.clear();
    }

    private void cancelInput() {
        targets.clear();
        field.repaintAll();
    }

    public void mousePressed(int button, Point point) {
        if (drawingState != 3) return;
        if (button == MouseEvent.BUTTON1) {
            if (inputActive() && rubberMode) {
                cancelInput();
                drawingState = 0;
            } else {
                addTarget(point);
            }
        } else if (button == MouseEvent.BUTTON3) {
            if (inputActive() && !rubberMode) {
                cancelInput();
                drawingState = 0;
            } else {
                rubberMode = true;
                addTarget(point);
            }
        }
    }

    public void mouseReleased(int button, Point point) {
        if (button == MouseEvent.BUTTON1) {
            drawingState |= 1;
            if (inputActive() && !rubberMode) {
                addTarget(point);
                confirmPainting();
            }
        } else if (button == MouseEvent.BUTTON3) {
            drawingState |= 2;
            if (inputActive() && rubberMode) {
                addTarget(point);
                confirmPainting();
                field.repaintAll();
            }
            rubberMode = false;
        }
    }

    public void mouseWheelMoved(int value) {
        brushSize = Math.max(1, brushSize + value);
    }

    public void mouseDragged(Point point) {

    }

    public void paintTargets(Graphics g) {
        for (Point point : targets) {
            fillCircle(point, brushSize, getDrawingColor(), g);
        }
    }

    public void paintMarkers(Point mousePosition, Graphics g) {
        for (Point point : targets) {
            fillCircle(point, brushSize, getDrawingColor(), g);
        }

        paintMouse(mousePosition, g);
    }

    public void paintMouse(Point mousePosition, Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(1));
        if (mousePosition != null) {
            fillCircle(mousePosition, brushSize, getMouseColor(), g);
            drawCircle(mousePosition, brushSize, inversedColor(getDrawingColor()), g);
        }
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public void changeMarkersMode() {
        markersMode = !markersMode;
    }

    public int getBrushSize() {
        return brushSize;
    }

    public boolean inRubberMode() {
        return rubberMode;
    }

    public boolean inFillMode() {
        return fillMode;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

}
