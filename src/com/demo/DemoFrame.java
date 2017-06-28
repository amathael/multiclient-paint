package com.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by jetbrains on 02/26/2017.
 */
public class DemoFrame extends JFrame {

    public static void main(String[] args) {
        new DemoFrame();
    }

    private boolean chatActive = true;

    private Container contentPane;
    private JPanel workspace, chat;

    private DemoFrame() throws HeadlessException {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setSize(new Dimension(900, 600));
        contentPane.setPreferredSize(new Dimension(900, 600));

        workspace = new JPanel();
        workspace.setBackground(Color.red);
        workspace.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (chatActive) {
                    hideChat();
                } else {
                    showChat();
                }
                repaint();
            }
        });

        new Thread(() -> {
            while (true) {
                System.err.println(workspace.getWidth());
            }
        }).start();

        chat = new JPanel();
        chat.setBackground(Color.blue);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 0;
        constraints.weighty = 1;

        constraints.gridx = 0;
        constraints.weightx = 1;
        contentPane.add(chat, constraints);
        constraints.gridx = 1;
        constraints.weightx = 2;
        contentPane.add(workspace, constraints);

        pack();
        setVisible(true);

    }

    private void hideChat() {
        chatActive = false;
        chat.setVisible(false);
    }

    private void showChat() {
        chatActive = true;
        chat.setVisible(true);
    }

}
