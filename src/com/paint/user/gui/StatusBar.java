package com.paint.user.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.paint.global.utils.Utils.inversedColor;
import static com.paint.global.utils.Utils.paintBorderedSquare;

/**
 * Created by jetbrains on 02/26/2017.
 */
class StatusBar extends JPanel {

    private static int PANEL_HEIGHT = 25;
    private int
            MARGIN = 5,
            TEXT_SIZE = 12,
            IMAGE_SIZE = PANEL_HEIGHT - 2 * MARGIN,
            TEXT_UNDERLINE = (PANEL_HEIGHT + TEXT_SIZE) / 2 - 1,
            COLOR_LEFT = MARGIN,
            SHAPE_LEFT = MARGIN + 250,
            RUBBER_LEFT;

    private final Font font = new Font("Arial", Font.PLAIN, TEXT_SIZE);
    private final Color BACKGROUND_COLOR = Color.lightGray;

    private FieldPanel field;

    StatusBar(PaintPanel parent, int width) {
        field = parent.getFieldPanel();
        setDoubleBuffered(true);
        setBackground(BACKGROUND_COLOR);
        RUBBER_LEFT = width - MARGIN - 100;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                parent.handleColorChooserActivator();
            }
        });

        setSize(new Dimension(width, PANEL_HEIGHT));
        setPreferredSize(new Dimension(width, PANEL_HEIGHT));
        setMinimumSize(new Dimension(width, PANEL_HEIGHT));
        setMaximumSize(new Dimension(width, PANEL_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintColorBlock(g);
        paintShapeBlock(g);
        if (field.getBrush().inRubberMode()) {
            paintRubberBlock(g);
        }
    }

    private void initFont(Graphics g) {
        g.setColor(inversedColor(field.getBackground()));
        g.setFont(font);
    }

    private void paintColorBlock(Graphics g) {
        Color color = field.getBrush().getColor();
        paintBorderedSquare(COLOR_LEFT, MARGIN, IMAGE_SIZE, color, g);
        initFont(g);
        g.drawString("Current Color ("
                        + color.getRed() + ", "
                        + color.getGreen() + ", "
                        + color.getBlue() + ")",
                COLOR_LEFT + MARGIN + IMAGE_SIZE, TEXT_UNDERLINE);
    }

    private void paintShapeBlock(Graphics g) {
        initFont(g);
        g.drawString("Mode: "
                        + field.getBrush()
                        + (field.getBrush().inFillMode() ? " (filled)" : "")
                        + (field.getBrush().getBrushSize() > 1 ? ", size " + field.getBrush().getBrushSize() : ""),
                SHAPE_LEFT + MARGIN, TEXT_UNDERLINE);
    }

    private void paintRubberBlock(Graphics g) {
        paintBorderedSquare(RUBBER_LEFT, MARGIN, IMAGE_SIZE, field.getBackground(), g);
        g.setColor(inversedColor(field.getBackground()));
        g.setFont(font);
        g.drawString("Rubber Active",
                RUBBER_LEFT + MARGIN + IMAGE_SIZE, TEXT_UNDERLINE);
    }

    static int staticHeight() {
        return PANEL_HEIGHT;
    }

}
