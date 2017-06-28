package com.paint.user.gui.brushes;

import com.paint.user.gui.FieldPanel;

import java.awt.*;

import static com.paint.global.utils.Utils.paintOval;

/**
 * Created by jetbrains on 03/03/2017.
 */
public class OvalBrush extends Brush {

    public OvalBrush(FieldPanel field) {
        super(field);
    }

    public OvalBrush(FieldPanel field, int brushSize) {
        super(field, brushSize);
    }

    @Override
    public void paintTargets(Graphics g) {
        if (targets.size() != 2) {
            throw new IllegalArgumentException("Incorrect targets size");
        } else {
            paintOval(targets.get(0), targets.get(1), brushSize, getDrawingColor(), fillMode, g);
        }
    }

    @Override
    public void paintMarkers(Point mousePosition, Graphics g) {
        if (targets.size() > 1) {
            throw new IllegalArgumentException("Incorrect targets size (markers type)");
        } else if (targets.size() == 1) {
            paintOval(targets.getFirst(), mousePosition, brushSize, getDrawingColor(), fillMode, g);
        } else {
            paintMouse(mousePosition, g);
        }
    }

}
