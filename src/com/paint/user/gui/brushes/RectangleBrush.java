package com.paint.user.gui.brushes;

import com.paint.user.gui.FieldPanel;

import java.awt.*;

import static com.paint.global.utils.Utils.*;

/**
 * Created by jetbrains on 03/03/2017.
 */
public class RectangleBrush extends Brush {

    public RectangleBrush(FieldPanel field) {
        super(field);
    }

    public RectangleBrush(FieldPanel field, int brushSize) {
        super(field, brushSize);
    }

    @Override
    public void paintTargets(Graphics g) {
        if (targets.size() != 2) {
            throw new IllegalArgumentException("Incorrect targets size");
        } else {
            paintRectangle(targets.get(0), targets.get(1), brushSize, getDrawingColor(), fillMode, g);
        }
    }

    @Override
    public void paintMarkers(Point mousePosition, Graphics g) {
        if (targets.size() > 1) {
            throw new IllegalArgumentException("Incorrect targets size (markers type)");
        } else if (targets.size() == 1) {
            paintRectangle(targets.getFirst(), mousePosition, brushSize, getDrawingColor(), fillMode, g);
        } else {
            paintMouse(mousePosition, g);
        }
    }

    @Override
    public void paintMouse(Point mousePosition, Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(1));
        if (mousePosition != null) {
            fillSquare(mousePosition, brushSize, getMouseColor(), g);
            drawSquare(mousePosition, brushSize, inversedColor(getDrawingColor()), g);
        }
    }

}
