package com.paint.user.gui.brushes;

import com.paint.user.gui.FieldPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static com.paint.global.utils.Utils.fillSpace;

/**
 * Created by jetbrains on 03/03/2017.
 */
public class FillBrush extends Brush {

    public FillBrush(FieldPanel fieldPanel) {
        super(fieldPanel);
    }

    public FillBrush(FieldPanel field, int brushSize) {
        super(field, brushSize);
    }

    @Override
    public void mousePressed(int button, Point point) {
        if (drawingState != 3) return;
        addTarget(point);
        if (button == MouseEvent.BUTTON1) {
            drawingState = 2;
        } else if (button == MouseEvent.BUTTON3) {
            rubberMode = true;
            drawingState = 1;
        }
        confirmPainting();
    }

    @Override
    public void mouseReleased(int button, Point point) {
        if (button == MouseEvent.BUTTON1) {
            drawingState |= 1;
        } else if (button == MouseEvent.BUTTON3) {
            drawingState |= 2;
            rubberMode = false;
        }
    }

    @Override
    public void paintTargets(Graphics g) {
        fillSpace(this.targets.getFirst(), getDrawingColor(), field.getField());
    }

    @Override
    public void paintMarkers(Point mousePosition, Graphics g) {
        if (markersMode) {
            BufferedImage filler = new BufferedImage(field.getFieldWidth(), field.getFieldHeight(), BufferedImage.TYPE_INT_ARGB);
            filler.getGraphics().drawImage(field.getField(), 0, 0, null);
            fillSpace(mousePosition, getMouseColor(), filler);
            g.drawImage(filler, 0, 0, null);
        }
    }

    @Override
    public int getBrushSize() {
        return 1;
    }

    @Override
    public boolean inRubberMode() {
        return false;
    }

    @Override
    public boolean inFillMode() {
        return false;
    }

}
