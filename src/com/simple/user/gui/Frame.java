package com.simple.user.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by jetbrains on 02/27/2017.
 */
public class Frame extends JFrame {

    private int WIDTH = 900, HEIGHT = 600;

    public Frame() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));

        pack();
        setVisible(true);

        relocateToCenter();
    }

    private void relocateToCenter() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setLocation(new Point((int) (screenSize.getWidth() - getWidth()) / 2, (int) (screenSize.getHeight() - getHeight()) / 2));
    }

    public static void main(String[] args) {
        new Frame();
    }

}
