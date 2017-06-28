package com.paint.user.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by jetbrains on 02/26/2017.
 */
class ColorChooser extends JPanel {

    private static int PANEL_WIDTH = 200;

    ColorChooser(FieldPanel field) {
        setDoubleBuffered(true);
        setBackground(Color.white);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                field.setBackground(new Color(e.getX() % 256, e.getY() % 256, e.getClickCount() * 50 % 256));
            }
        });
    }

    static int staticWidth() {
        return PANEL_WIDTH;
    }

}
