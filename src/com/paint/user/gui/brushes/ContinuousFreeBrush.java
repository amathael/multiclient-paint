package com.paint.user.gui.brushes;

import com.paint.user.gui.FieldPanel;

import java.awt.*;

import static com.paint.global.utils.Utils.*;
import static com.paint.global.utils.Utils.inversedColor;

/**
 * Created by jetbrains on 03/03/2017.
 */
public class ContinuousFreeBrush extends Brush {

    public ContinuousFreeBrush(FieldPanel field) {
        super(field);
    }

    public ContinuousFreeBrush(FieldPanel field, int brushSize) {
        super(field, brushSize);
    }

    @Override
    public void mouseDragged(Point point) {
        if (drawingState != 3) return;
        addTarget(point);
    }

    @Override
    public void paintTargets(Graphics g) {
        if (targets.size() == 1) {
            fillCircle(targets.getFirst(), brushSize, getDrawingColor(), g);
        } else {
            for (int i = 0; i < targets.size() - 1; i++) {
                paintLine(targets.get(i), targets.get(i + 1), brushSize, getDrawingColor(), g);
            }
        }
    }

    @Override
    public void paintMarkers(Point mousePosition, Graphics g) {
        if (targets.size() == 1) {
            fillCircle(targets.getFirst(), brushSize, getDrawingColor(), g);
        } else {
            for (int i = 0; i < targets.size() - 1; i++) {
                paintLine(targets.get(i), targets.get(i + 1), brushSize, getDrawingColor(), g);
            }
        }

        paintMouse(mousePosition, g);
    }

}
