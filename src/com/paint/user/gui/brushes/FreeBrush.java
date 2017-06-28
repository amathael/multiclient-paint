package com.paint.user.gui.brushes;

import com.paint.user.gui.FieldPanel;

import java.awt.*;

/**
 * Created by jetbrains on 03/03/2017.
 */
public class FreeBrush extends Brush {

    public FreeBrush(FieldPanel field) {
        super(field);
    }

    public FreeBrush(FieldPanel field, int brushSize) {
        super(field, brushSize);
    }

    @Override
    public void mouseDragged(Point point) {
        if (drawingState != 3) return;
        addTarget(point);
    }

}
