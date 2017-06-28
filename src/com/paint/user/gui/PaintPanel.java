package com.paint.user.gui;

import javax.swing.*;
import java.awt.*;

import static com.paint.user.gui.Frame.innerPad;

/**
 * Created by jetbrains on 02/27/2017.
 */
public class PaintPanel extends JPanel {

    private int
            STATUSBAR_HEIGHT = StatusBar.staticHeight(),
            FIELD_HEIGHT,
            COLOR_CHOOSER_WIDTH = ColorChooser.staticWidth(),
            FIELD_WIDTH;

    private SpringLayout layout;

    private StatusBar statusBar;
    private JPanel workspacePanel;
    private ColorChooser colorChooser;
    private FieldPanel field;

    PaintPanel(Frame frame, int width, int height, Color background) {
        setDoubleBuffered(true);
        setBackground(background);
        FIELD_HEIGHT = height - STATUSBAR_HEIGHT;
        FIELD_WIDTH = width - COLOR_CHOOSER_WIDTH;

        field = new FieldPanel(frame, width, FIELD_HEIGHT);
        statusBar = new StatusBar(this, width);
        field.bindStatusBar(statusBar);
        colorChooser = new ColorChooser(field);

        constructWorkspacePanel();
        workspacePanel.setBackground(background);
        layout = new SpringLayout();
        setLayout(layout);

        add(statusBar);
        add(workspacePanel);

        layout.putConstraint(SpringLayout.WEST, statusBar, innerPad, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, statusBar, innerPad, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, statusBar, -innerPad, SpringLayout.EAST, this);

        layout.putConstraint(SpringLayout.WEST, workspacePanel, innerPad, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, workspacePanel, innerPad, SpringLayout.SOUTH, statusBar);
        layout.putConstraint(SpringLayout.EAST, workspacePanel, -innerPad, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, workspacePanel, -innerPad, SpringLayout.SOUTH, this);

        colorChooser.setVisible(false);
    }

    private void constructWorkspacePanel() {
        workspacePanel = new JPanel();

        workspacePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 0;
        constraints.weighty = 1;

        constraints.gridx = 0;
        constraints.weightx = FIELD_WIDTH;
        workspacePanel.add(field, constraints);

        constraints.gridx = 1;
        constraints.weightx = COLOR_CHOOSER_WIDTH;
        constraints.insets = new Insets(0, 1, 0, -1);
        workspacePanel.add(colorChooser, constraints);
    }

    FieldPanel getFieldPanel() {
        return field;
    }

    void handleColorChooserActivator() {
        colorChooser.setVisible(!colorChooser.isVisible());
    }

}
