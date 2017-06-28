package com.paint.user.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by jetbrains on 02/26/2017.
 */
class ExtensionsPanel extends JPanel {

    private static int PANEL_HEIGHT = 25;

    ExtensionsPanel(Frame frame, int width) {
        setDoubleBuffered(true);
        setBackground(Color.white);

        setSize(new Dimension(width, PANEL_HEIGHT));
        setPreferredSize(new Dimension(width, PANEL_HEIGHT));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                frame.handleChatActivator();
            }
        });
    }

    static int staticHeight() {
        return PANEL_HEIGHT;
    }

}
